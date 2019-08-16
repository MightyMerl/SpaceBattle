package mgrosseh.spacebattle.io;

import mgrosseh.spacebattle.Constants;
import mgrosseh.spacebattle.Game;
import mgrosseh.spacebattle.displaying.Auto;
import mgrosseh.spacebattle.displaying.WindowAction;
import processing.core.PImage;
import processing.core.PVector;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

public class Sprite implements Constants {

    public String name;

    public int type;

    public boolean isParent, isChild;

    public float x, y;
    public float strokeWeight;
    public int fill, stroke;
    public boolean close;
    public PVector[] vectors;
    public int vectorNum;

    public Sprite[] children;
    public int childrenNum;

    public PImage image;
    public String text;

    public float width, height;
    public float x2, y2;

    public static float start;
    private static int averageArgs = -1;

    private static String contents;

    private static int[] memA = new int[SCAN_MAX_MEMORY];
    private static float[] memB = new float[SCAN_MAX_MEMORY];
    private static float ifMem = 0;

    private static HashMap<String, Float> floatVars = new HashMap<>();

    private static HashMap<String, Integer> jumpNames = new HashMap<>();

    private static boolean shape = false, jump = false, condition = false;
    private static boolean group = false;

    private static final int maxSameJump = 5000;
    private static int jumpLine = 0, lastJump = 0, sameJump = 0;

    private static Sprite out = new Sprite();

    private static int fillF = 0, strokeF = 0;
    private static float strokeWeightF = 1, xF = 0, yF = 0, x2F = 0, y2F = 0, widthF = 1, heightF = 1;
    private static int skipLines = 0;

    private static String[] lines;
    private static String[] tokens;
    private static int lineNum = 0;
    private static int tokenNum = 0;
    private static boolean aborted = false;

    private static final String SEPARATION = "[+\\-*/%()=<>&|:]"; //separation characters
    private static final String RESERVED = "(true)|(false)";
    private static final String[] FUNCTIONS = {
            "sprite_group",
            "sprite",
            "fill",
            "stroke",
            "strokeWeight",
            "x",
            "y",
            "width",
            "height",
            "translate",
            "rect",
            "anim",
            "ellipse",
            "image",
            "line",
            "point",
            "shape",
            "text",
            "jump",
            "log",
            "if"
    };

    public Sprite() {
        children = new Sprite[0];
        vectors = new PVector[0];
        name = UNKNOWN_NAME;
        type = UNKNOWN_TYPE;
        x = -1;
        y = -1;
        x2 = -1;
        y2 = -1;
        width = 1;
        height = 1;
    }


    static Sprite convert(File file, Game game) { //intentionally package private
        try {
            start = System.nanoTime() / SECOND_TO_NANOSECONDS;
            aborted = false;
            try {
                Scanner myScanner = new Scanner(file);
                contents = myScanner.useDelimiter("\\Z").next();
                myScanner.close();
                contents = contents.replace(";", "\n").replace(",", " ").replaceAll("\\s+", " ").replace("\n ", "\n");
                if (game.isLogOn()) System.out.println("\nLoading file \"" + file.getName() + "\"\n");

                lines = contents.split("\n");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            //finding jump points
            while (lineNum < lines.length) {
                tokens = tokenize(lines[lineNum]).split(" ");
                if (tokens[0].startsWith("#")) {
                    jumpNames.put(tokens[0], lineNum);
                }
                lineNum++;
            }
            lineNum = 0;

            //Defining vars
            shape = false;
            jump = false;
            condition = false;
            group = false;

            jumpLine = 0;
            lastJump = 0;
            sameJump = 0;

            out = new Sprite();

            fillF = 0;
            strokeF = 0;
            strokeWeightF = 1;
            xF = 0;
            yF = 0;
            x2F = 0;
            y2F = 0;
            widthF = 1;
            heightF = 1;
            skipLines = 0;

            averageArgs = 0;

            while (lineNum < lines.length) {

                tokens = tokenize(lines[lineNum]).split(" ");

                if (skipLines >= 1) {
                    skipLines--;
                    continue;
                }
                tokenNum = 0;
                while (tokenNum < tokens.length) evaluateLine(game);
                if (aborted) return out;
                lineNum++;
            }
            if (game.isLogOn())
                System.out.println("Successfully loaded sprite data of \"" + out.name + "\" in " + (System.nanoTime() / SECOND_TO_NANOSECONDS - start) + " seconds.");
            return out;
        } catch (Exception e) {
            if (game.isLogOn()) {
                System.out.println("Following error occurred whilst trying to load a sprite - sprite not loaded:\n");
                e.printStackTrace();
            }
            return out;
        }
    }

    private static void spriteGroup() {
        String fullName = subarrayToString(tokens, 0, tokens.length).replace("\"", "") + " ";
        tokenNum = tokens.length;
        if (fullName.startsWith(" ")) fullName = fullName.substring(1);
        if (fullName.endsWith(" ")) fullName = fullName.trim();
        out.name = fullName;
        out.makeGroup();
        group = true;
        if (out.name.equals(UNKNOWN_NAME)) {
            System.out.println("WARNING: Sprite name is invalid. Sprite aborted...");
            aborted = true;
        }
    }

    private static void sprite() {
        out.name = tokens[tokenNum++].replace("\"", "");
        group = false;
        if (out.name.equals(UNKNOWN_NAME)) {
            System.out.println("WARNING: Sprite name is invalid. Sprite aborted...");
            aborted = true;
        }
    }

    private static void fill() {
        fillF = (int) getNumberInScan(reduceStatement(subarrayToString(tokens, tokenNum++, tokens.length)));
        if (!group) out.fill = fillF;
    }

    private static void stroke() {
        strokeF = (int) getNumberInScan(reduceStatement(subarrayToString(tokens, tokenNum++, tokens.length)));
        if (!group) out.stroke = strokeF;
    }

    private static void strokeWeight() {
        strokeWeightF = (int) getNumberInScan(reduceStatement(subarrayToString(tokens, tokenNum++, tokens.length)));
        if (!group) out.strokeWeight = strokeWeightF;
    }

    private static void x() {
        out.x = (int) getNumberInScan(reduceStatement(subarrayToString(tokens, tokenNum++, tokens.length)));
    }

    private static void y() {
        out.y = (int) getNumberInScan(reduceStatement(subarrayToString(tokens, tokenNum++, tokens.length)));
    }

    private static void width() {
        widthF = (int) getNumberInScan(reduceStatement(subarrayToString(tokens, tokenNum++)));
    }

    private static void height() {
        heightF = (int) getNumberInScan(reduceStatement(subarrayToString(tokens, tokenNum++)));
    }

    private static void translate() {
        int start = startOfBracket(subarrayToString(tokens, tokenNum), '(') + tokenNum;
        int end = endOfBracket(subarrayToString(tokens, tokenNum), '(', ')') + tokenNum;
        tokenNum = end + 1;
        xF = (int) getNumberInScan(reduceStatement(subarrayToString(tokens, start, end)));
        start = startOfBracket(subarrayToString(tokens, tokenNum), '(') + tokenNum;
        end = endOfBracket(subarrayToString(tokens, tokenNum), '(', ')') + tokenNum;
        tokenNum = end + 1;
        yF = (int) getNumberInScan(reduceStatement(subarrayToString(tokens, start, end)));
        if (!group) out.x = xF;
        if (!group) out.y = yF;
    }

    private static void rect() {
        if (group) {
            addChildWithType(out, RECT, xF, yF, widthF, heightF, fillF, strokeF, strokeWeightF, tokens);
        } else {
            setTypeWithArgs(out, RECT, xF, yF, widthF, heightF, tokens);
        }
    }

    private static void animation() {
        if (group) {
            addChildWithType(out, ANIMATION, xF, yF, widthF, heightF, fillF, strokeF, strokeWeightF, tokens);
        } else {
            setTypeWithArgs(out, ANIMATION, xF, yF, widthF, heightF, tokens);
        }
    }

    private static void ellipse() {
        if (group) {
            addChildWithType(out, ELLIPSE, xF, yF, widthF, heightF, fillF, strokeF, strokeWeightF, tokens);
        } else {
            setTypeWithArgs(out, ELLIPSE, xF, yF, widthF, heightF, tokens);
        }
    }

    private static void image(Game game) {
        if (group) {
            Sprite image = new Sprite();
            image.image = game.loadImage(tokens[tokenNum++].replace("\"", ""));
            addChildWithType(out, image, IMAGE, xF, yF, widthF, heightF, fillF, strokeF, strokeWeightF, tokens);
        } else {
            out.image = game.loadImage(tokens[tokenNum++].replace("\"", ""));
            setTypeWithArgs(out, IMAGE, xF, yF, widthF, heightF, tokens);
        }
    }

    private static void line() {
        if (group) {
            addChildWithType(out, LINE, xF, yF, x2F, y2F, fillF, strokeF, strokeWeightF, tokens);
        } else {
            setTypeWithArgs(out, LINE, xF, yF, x2F, y2F, tokens);
        }
    }

    private static void point() {
        if (group) {
            addChildWithType(out, POINT, xF, yF, 0, 0, fillF, strokeF, strokeWeightF, tokens);
        } else {
            setTypeWithArgs(out, POINT, xF, yF, 0, 0, tokens);
        }
    }

    private static void shape() {
        if (group) {
            Sprite child = new Sprite();
            if (tokenNum < tokens.length) child.name = tokens[tokenNum++];
            addChildWithType(out, child, SHAPE, xF, yF, x2F, y2F, fillF, strokeF, strokeWeightF, tokens);
        } else {
            if (tokenNum < tokens.length) out.name = tokens[tokenNum++];
            setTypeWithArgs(out, SHAPE, xF, yF, x2F, y2F, tokens);
        }
    }

    private static void text() {
        if (group) {
            Sprite text = new Sprite();
            text.text = tokens[tokenNum++].replace("\"", "").replace("_", " ");
            addChildWithType(out, text, TEXT, xF, yF, widthF, heightF, fillF, strokeF, strokeWeightF, tokens);
        } else {
            out.text = tokens[tokenNum++].replace("\"", "").replace("_", " ");
            setTypeWithArgs(out, TEXT, xF, yF, widthF, heightF, tokens);
        }
    }

    private static void jump() {
        if (tokenNum < tokens.length) {
            String string = tokens[tokenNum++];
            if (isNumber(string)) {
                if (string.startsWith("+")) {
                    skipLines = (int) getNumberInScan(string);
                } else if (string.startsWith("-")) {
                    jump = true;
                    jumpLine = lineNum + (int) getNumberInScan(string);
                } else {
                    jump = true;
                    jumpLine = (int) getNumberInScan(string);
                }
            }
        } else skipLines = 1;
    }

    private static void log(Game game) {
        while (tokenNum < tokens.length) {
            String next = tokens[tokenNum++];
            if (game.isLogOn()) {
                System.out.print((isNumber(next) ? getNumberInScan(next) : isMemory(next) ? getMemoryValue(next) : next) + " ");
            }
        }
        System.out.println();
    }

    private static boolean checkCondition(Game game) {
        String total = "";
        while (tokenNum < tokens.length) {
            total += subarrayToString(tokens, tokenNum);
        }

        int end = endOfBracket(total, '(', ')');
        String value = total.substring(startOfBracket(total, '('), end);

        value = reduceStatement(value);

        if (getTruthValue(value)) condition = true;
        else condition = false;

        if (condition && !total.matches(".*\\)")) {
            String[] copy = tokens;
            tokens = new String[tokens.length - tokenNum];
            for (int i = tokenNum; i < tokens.length; i++) {
                tokens[i] = copy[i + tokenNum];
            }
            tokenNum = 0;
            condition = false;
            return true;
        }
        return false;
    }

    private static int startOfBracket(String input, char open) {
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '(') return i;
        }
        return -1;
    }

    private static int endOfBracket(String input, char open, char close) {
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '(') {
                int count = -1;
                int j = i + 1;
                while (count < 0 && j < input.length()) {
                    if (input.charAt(j) == open) {
                        count--;
                    } else if (input.charAt(j) == close) {
                        count++;
                    }
                    j++;
                }
                if (j > input.length()) return input.length();
                return j;
            }
        }
        return -1;
    }

    private static String reduceStatement(String statement) {
        String lastWord = "";
        String[] words = tokenize(statement).split(" ");
        while (subarrayToString(words, 0, words.length).matches(".*\\(.*\\).*")) {
            for (int i = 0; i < words.length; i++) {
                if (words[i].equals("(")) {
                    int count = -1;
                    int j = i + 1;
                    while (count < 0 && j < words.length) {
                        if (words[j].equals("(")) {
                            count--;
                        } else if (words[j].equals(")")) {
                            count++;
                        }
                        j++;
                    }
                    if (j > words.length) return null;
                    int end = j;
                    String newStatement = subarrayToString(words, i + 1, end - 1);
                    newStatement = reduceStatement(newStatement);
                    words = cutAndInsert(words, i, end - 1, newStatement);
                }
            }
        }
        while (subarrayToString(words, 0, words.length).matches(".*([*/%]).*")) {
            for (int i = 0; i < words.length; i++) {
                if (words[i].matches(SEPARATION)) {
                    lastWord = words[i];
                } else {
                    if (words[i].matches(RESERVED)) {

                    }
                    if (!lastWord.equals("")) {
                        switch (lastWord) {
                            case "*":
                                words = cutAndInsert(words, i - 2, i, (getNumberInScan(words[i - 2]) * getNumberInScan(words[i])) + "");
                                break;
                            case "/":
                                words = cutAndInsert(words, i - 2, i, (getNumberInScan(words[i - 2]) / getNumberInScan(words[i])) + "");
                                break;
                            case "%":
                                words = cutAndInsert(words, i - 2, i, (getNumberInScan(words[i - 2]) % getNumberInScan(words[i])) + "");
                                break;
                        }
                        lastWord = "";
                    }
                }
            }
        }
        while (subarrayToString(words, 0, words.length).matches(".*([+]|[-]).*")) {
            for (int i = 0; i < words.length; i++) {
                if (words[i].matches(SEPARATION)) {
                    lastWord = words[i];
                } else {
                    if (words[i].matches(RESERVED)) {

                    }
                    if (!lastWord.equals("")) {
                        switch (lastWord) {
                            case "+":
                                words = cutAndInsert(words, i - 2, i, (getNumberInScan(words[i - 2]) + getNumberInScan(words[i])) + "");
                                break;
                            case "-":
                                words = cutAndInsert(words, i - 2, i, (getNumberInScan(words[i - 2]) - getNumberInScan(words[i])) + "");
                                break;
                        }
                        lastWord = "";
                    }
                }
            }
        }
        while (subarrayToString(words, 0, words.length).matches(".*([=][=]|<|>|[<][=]|[>][=]).*")) {
            for (int i = 0; i < words.length; i++) {
                if (words[i].matches(SEPARATION)) {
                    lastWord = words[i];
                } else {
                    if (words[i].matches(RESERVED)) {

                    }
                    if (!lastWord.equals("")) {
                        switch (lastWord) {
                            case "==":
                                words = cutAndInsert(words, i - 2, i, (getNumberInScan(words[i - 2]) == getNumberInScan(words[i])) + "");
                                break;
                            case "<":
                                words = cutAndInsert(words, i - 2, i, (getNumberInScan(words[i - 2]) < getNumberInScan(words[i])) + "");
                                break;
                            case ">":
                                words = cutAndInsert(words, i - 2, i, (getNumberInScan(words[i - 2]) > getNumberInScan(words[i])) + "");
                                break;
                            case "<=":
                                words = cutAndInsert(words, i - 2, i, (getNumberInScan(words[i - 2]) <= getNumberInScan(words[i])) + "");
                                break;
                            case ">=":
                                words = cutAndInsert(words, i - 2, i, (getNumberInScan(words[i - 2]) >= getNumberInScan(words[i])) + "");
                                break;
                        }
                        lastWord = "";
                    }
                }
            }
        }
        while (subarrayToString(words, 0, words.length).matches(".*([!|&]).*")) {
            for (int i = 0; i < words.length; i++) {
                if (words[i].matches(SEPARATION)) {
                    lastWord = words[i];
                } else {
                    if (words[i].matches(RESERVED)) {

                    }
                    if (!lastWord.equals("")) {
                        switch (lastWord) {
                            case "|":
                                words = cutAndInsert(words, i - 2, i, (Boolean.parseBoolean(words[i - 2]) || Boolean.parseBoolean(words[i])) + "");
                                break;
                            case "&":
                                words = cutAndInsert(words, i - 2, i, (Boolean.parseBoolean(words[i - 2]) && Boolean.parseBoolean(words[i])) + "");
                                break;
                            case "!":
                                words = cutAndInsert(words, i - 1, i, (!Boolean.parseBoolean(words[i])) + "");
                                break;
                        }
                        lastWord = "";
                    }
                }
            }
        }

        return subarrayToString(words, 0, words.length);
    }

    private static String tokenize(String statement) {
        String tokenized = "";
        String current = "";
        boolean lastSeparation = true;
        statement = statement.replace(" ", "");

        for (int i = 0; i < statement.length() + 1; i++) {
            if (i > 0) current += statement.charAt(i - 1);
            if (i == statement.length()) {
                tokenized += current;
            } else if ((statement.charAt(i) + "").matches(SEPARATION)) {
                char c = statement.charAt(i);
                if (lastSeparation) {
                    if (!((current + c).equals("==") || (current + c).equals(">=") || (current + c).equals("<=") || i == 0)) {
                        tokenized += current + " ";
                        current = "";
                    }
                } else {
                    tokenized += current + " ";
                    current = "";
                }
                lastSeparation = true;
            } else {
                if (lastSeparation && i != 0) {
                    tokenized += current + " ";
                    current = "";
                }
                lastSeparation = false;
            }
        }
        return tokenized;
    }

    private static String[] cutAndInsert(String[] array, int start, int end, String insert) {
        String[] all;

        if (start - 1 >= 0 && end + 1 < array.length) if (array[start].equals("(") && array[end + 1].equals(")")) {
            start--;
            end++;
        }
        int len = end - start + 1;
        all = new String[array.length - len + 1];
        for (int i = 0; i < array.length; i++) {
            if (i < start) {
                all[i] = array[i];
            } else if (i == start) {
                all[i] = insert;
            } else if (i > end) {
                all[i - len + 1] = array[i];
            }
        }
        return tokenize(subarrayToString(all, 0, all.length)).split(" ");
    }

    private static String subarrayToString(String[] array, int start) {
        return subarrayToString(array, start, array.length);
    }

    private static String subarrayToString(String[] array, int start, int end) {
        String total = "";
        for (int i = start; i < Math.min(array.length, end); i++) {
            total += array[i] + (i == Math.min(array.length, end) - 1 ? "" : " ");
        }
        return total;
    }

    private static String getArgs(String[] tokens) {

    }

    private static void evalVars() {
        String total = subarrayToString(tokens, 0, tokens.length);
        if (isMemory(total)) {
            writeMemory(0, getMemoryType(total), getMemoryAddr(total));
        }
        if (total.matches("\\$[AB][0-" + (SCAN_MAX_MEMORY - 1) + "][+][+]*")) {
            writeMemory(getMemoryValue(tokens[tokenNum]) + 1, getMemoryType(tokens[tokenNum]), getMemoryAddr(tokens[tokenNum]));
        }
        if (total.matches("\\$[AB][0-" + (SCAN_MAX_MEMORY - 1) + "][-][-]*")) {
            writeMemory(getMemoryValue(tokens[tokenNum]) - 1, getMemoryType(tokens[tokenNum]), getMemoryAddr(tokens[tokenNum]));
        }
        if (total.matches("\\$[AB][0-" + (SCAN_MAX_MEMORY - 1) + "]\\s*=\\s*[(].*")) {
            String val = reduceStatement(total.substring(startOfBracket(total, '('), endOfBracket(total, '(', ')')));
            writeMemory(getNumberInScan(val), getMemoryType(tokens[tokenNum]), getMemoryAddr(tokens[tokenNum]));
        } else if (total.matches("\\$[AB][0-" + (SCAN_MAX_MEMORY - 1) + "]\\s*=.*")) {
            String val = reduceStatement(total.substring(total.indexOf('=') + 1));
            writeMemory(getNumberInScan(val), getMemoryType(tokens[tokenNum]), getMemoryAddr(tokens[tokenNum]));
        }
    }

    private static boolean evaluateLine(Game game) {
        String token = tokens[tokenNum++];
        if (token.startsWith("//")) return false;
        if (condition) {
            if (token.equals("fi")) condition = false;
            else return false;
        }

        if (token.matches("\\$[AB][0-" + (SCAN_MAX_MEMORY - 1) + "].*")) {
            evalVars();
        } else if (contains(FUNCTIONS, token)) switch (token) {
            case "sprite_group":
                spriteGroup();
                break;
            case "sprite":
                sprite();
                break;
            case "fill":
                fill();
                break;
            case "stroke":
                stroke();
                break;
            case "strokeWeight":
                strokeWeight();
                break;
            case "x":
                x();
                break;
            case "y":
                y();
                break;
            case "width":
                width();
                break;
            case "height":
                height();
                break;
            case "translate":
                translate();
                break;
            case "rect":
                rect();
                break;
            case "anim":
                animation();
                break;
            case "ellipse":
                ellipse();
                break;
            case "image":
                image(game);
                break;
            case "line":
                line();
                break;
            case "point":
                point();
                break;
            case "shape":
                shape();
                break;
            case "text":
                text();
                break;
            case "jump":
                jump();
                break;
            case "log":
                log(game);
                break;
            case "if":
                if (checkCondition(game)) {
                    return false;
                }
                break;
        }
        if (jump)
            if (sameJump <= maxSameJump) {
                if (lastJump == lineNum) sameJump++;
                else sameJump = 0;
                lastJump = lineNum;
                lineNum = jumpLine;
                jump = false;

            } else {
                System.out.println("WARNING: Looped to same location too often, skipping loop...");
                jump = false;
                sameJump = 0;
                lineNum++;
            }
        return false;
    }

    private static boolean contains(Object[] objects, Object object) {
        for (Object obj : objects) {
            if (obj.equals(object)) return true;
        }
        return false;
    }

    private static float getNumberInScan(String token) {
        if (token.startsWith("0x")) {
            return hexToInt(token);
        }
        if (isRealNumber(token)) {
            return Float.parseFloat(token);
        }
        if (isMemory(token)) {
            return getMemoryValue(token);
        }
        if (isLineAddress(token)) {
            if (jumpNames.keySet().contains(token)) return jumpNames.get(token);
        }
        if (isTruthValue(token)) {
            return getTruthValue(token) ? 1 : 0;
        }
        if (floatVars.keySet().contains(token)) {
            return floatVars.get(token);
        }
        return 0;
    }

    private static boolean getTruthValue(String token) {
        return token.equals("true") || token.equals("1");
    }

    private static boolean isTruthValue(String token) {
        return token.equals("true") || token.equals("false") || token.equals("1") || token.equals("0");
    }

    private static boolean isHexNumber(String number) {
        return number.matches("0x[0-9a-fA-F]+");
    }

    private static boolean isRealNumber(String number) {
        return number.matches("[-+]*\\d*\\.*\\d+");
    }

    private static boolean isNumber(String number) {
        return isRealNumber(number) || isLineAddress(number) || isMemory(number) || isHexNumber(number);
    }

    private static boolean isMemory(String name) {
        return name.matches("\\$[AB][0-" + (SCAN_MAX_MEMORY - 1) + "]");
    }

    private static boolean isLineAddress(String name) {
        return name.matches("#[a-zA-Z_0-9]+");
    }

    private static float getMemoryValue(String address) {
        int type = getMemoryType(address);
        int addr = getMemoryAddr(address);
        if (type == 0) {
            return memA[addr];
        } else {
            return memB[addr];
        }
    }

    private static int getMemoryType(String address) {
        return address.charAt(1) == 'A' ? 0 : 1;
    }

    private static int getMemoryAddr(String address) {
        return Integer.parseInt(address.charAt(2) + "");
    }

    private static void writeMemory(float value, int type, int addr) {
        if (type == 0) {
            memA[addr] = (int) value;
        } else {
            memB[addr] = value;
        }
        //System.out.println("Wrote " + value + " in memory " + (type==0?"A":"B") + addr + ".");
    }

    private static int hexToInt(String next) {
        next = next.toLowerCase();
        if (!next.startsWith("0x")) return 0;
        next = next.substring(2);
        return (int) Long.parseLong(next, 16);
    }

    private static void addChildWithType(Sprite out, int type, float x, float y, float width, float height,
                                         int fill, int stroke, float strokeWeight, String[] tokens) {
        addChildWithType(out, new Sprite(), type, x, y, width, height, fill, stroke, strokeWeight, tokens);
    }

    private static void addChildWithType(Sprite out, Sprite child, int type, float x, float y, float width, float height,
                                         int fill, int stroke, float strokeWeight, String[] tokens) {
        child.stroke = stroke;
        child.fill = fill;
        child.strokeWeight = strokeWeight;
        child.isChild = true;
        setTypeWithArgs(child, type, x, y, width, height, tokens);
        out.addChild(child);
    }

    private static void setTypeWithArgs(Sprite out, int type, float x, float y, float width, float height, String[] tokens) {
        out.assignType(type);
        if (tokenNum < tokens.length) {
            out.x = getNumberInScan(tokens[tokenNum++]);
        } else out.x = x;

        if (tokenNum < tokens.length) {
            out.y = getNumberInScan(tokens[tokenNum++]);
        } else out.y = y;

        if (tokenNum < tokens.length) {
            if (type == LINE) out.x2 = getNumberInScan(tokens[tokenNum++]);
            else out.width = getNumberInScan(tokens[tokenNum++]);
        } else {
            if (type == LINE) out.x2 = width;
            else out.width = width;
        }

        if (tokenNum < tokens.length) {
            if (type == LINE) out.y2 = getNumberInScan(tokens[tokenNum++]);
            else out.height = getNumberInScan(tokens[tokenNum++]);
        } else {
            if (type == LINE) out.y2 = height;
            else out.height = height;
        }

        if (type == SHAPE) {
            int index = 0;
            float xCoord = 0;
            while (tokenNum < tokens.length) {
                String next = tokens[tokenNum++].replace(",", "");
                if (next.equals("") || next.matches("\\s*")) continue;
                float coordinate = getNumberInScan(next);
                if (index % 2 == 0) {
                    xCoord = coordinate;
                } else {
                    out.addVector(new PVector(xCoord, coordinate));
                }
                index++;
            }
        }
    }

    public WindowAction getAction() {
        return new Auto(this);
    }

    public void makeGroup() {
        isParent = true;
        type = GROUP;
    }

    public void assignType(int type) {
        if (type != GROUP) isParent = true;
        this.type = type;
    }

    public void addChild(Sprite child) {
        Sprite[] newChildren = new Sprite[children.length + 1];
        for (int i = 0; i < children.length; i++) {
            newChildren[i] = children[i];
        }
        newChildren[children.length] = child;
        children = newChildren;
        childrenNum++;
    }

    public void addVector(PVector vector) {
        PVector[] newVectors = new PVector[vectors.length + 1];
        for (int i = 0; i < vectors.length; i++) {
            newVectors[i] = vectors[i];
        }
        newVectors[vectors.length] = vector;
        vectors = newVectors;
        vectorNum++;
    }

    public String toString() {
        String string = "Sprite: \"" + name + "\", x: " + x + "; y: " + y + "; type: " + typeToString(type);
        if (type == GROUP) {
            string += " (" + childrenNum + " children [\n";
            for (int i = 0; i < childrenNum; i++) {
                if (children[i] == null) continue;
                string += "  " + children[i].toString() + "\n";
            }
            string += "]\n";
            string += ")";
        } else {
            string += " ( fill color: " + fill + "; stroke color: " + stroke + "; stroke weight: " + strokeWeight + "; width: " + width + "; height: " + height + ")";
        }
        return string;
    }

    private String typeToString(int type) {
        return type == NONE ? "None" : type == ANIMATION ? "Animation" : type == IMAGE ? "Image" : type == SHAPE ? "Shape" : type == TEXT ? "Text" : type == RECT ? "Rectangle" : type == ELLIPSE ? "Ellipse" : type == LINE ? "Line" : type == GROUP ? "Group" : "Unknown";
    }
}
