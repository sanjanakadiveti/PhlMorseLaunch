package phlmorse.gatech.edu.phlmorse.model;

/**
 * Created by sanjanakadiveti on 3/19/18.
 */

public class Quiz {
    //public static final ANSWERS
    private String word;
    public Quiz(String w) {
        word = w;
    }
    public String[] getLetters() {
        String[] ret = new String[word.length()];
        for (int i = 0; i < word.length(); i++) {
            ret[i] = Character.toString(word.charAt(i));
        }
        return ret;
    }
    public String getWord() {
        return word;
    }
    public static Boolean isCorrect(String tested, String userAnswer) {
        String morseAnswer = "";
        char[] testedLetters = tested.toCharArray();
        for (char c : testedLetters) {
            morseAnswer += getMorse(c);
        }
        return (userAnswer.equals(morseAnswer));
    }
    private static String getMorse(char c) {
        c = Character.toUpperCase(c);
        char[] morseCodeSignals = null;
        switch (c) {
            case 'A':
                morseCodeSignals = new char[] {
                        '.',
                        '-'
                };
                break;

            case 'B':
                morseCodeSignals = new char[] {
                        '-',
                        '.',
                        '.',
                        '.'
                };
                break;

            case 'C':
                morseCodeSignals = new char[] {
                        '-',
                        '.',
                        '-',
                        '.'
                };
                break;

            case 'D':
                morseCodeSignals = new char[] {
                        '-',
                        '.',
                        '.'
                };
                break;

            case 'E':
                morseCodeSignals = new char[] {
                        '.'
                };
                break;

            case 'F':
                morseCodeSignals = new char[] {
                        '.',
                        '.',
                        '-',
                        '.'
                };
                break;

            case 'G':
                morseCodeSignals = new char[] {
                        '-',
                        '-',
                        '.'
                };
                break;

            case 'H':
                morseCodeSignals = new char[] {
                        '.',
                        '.',
                        '.',
                        '.'
                };
                break;

            case 'I':
                morseCodeSignals = new char[] {
                        '.',
                        '.'
                };
                break;

            case 'J':
                morseCodeSignals = new char[] {
                        '.',
                        '-',
                        '-',
                        '-'
                };
                break;

            case 'K':
                morseCodeSignals = new char[] {
                        '-',
                        '.',
                        '-'
                };

            case 'L':
                morseCodeSignals = new char[] {
                        '.',
                        '-',
                        '.',
                        '.'
                };
                break;

            case 'M':
                morseCodeSignals = new char[] {
                        '-',
                        '-'
                };
                break;

            case 'N':
                morseCodeSignals = new char[] {
                        '-',
                        '.'
                };
                break;

            case 'O':
                morseCodeSignals = new char[] {
                        '-',
                        '-',
                        '-'
                };
                break;

            case 'P':
                morseCodeSignals = new char[] {
                        '.',
                        '-',
                        '-',
                        '.'
                };
                break;

            case 'Q':
                morseCodeSignals = new char[] {
                        '-',
                        '-',
                        '.',
                        '-'
                };
                break;

            case 'R':
                morseCodeSignals = new char[] {
                        '.',
                        '-',
                        '.'
                };
                break;

            case 'S':
                morseCodeSignals = new char[] {
                        '.',
                        '.',
                        '.'
                };
                break;

            case 'T':
                morseCodeSignals = new char[] {
                        '-'
                };
                break;

            case 'U':
                morseCodeSignals = new char[] {
                        '.',
                        '.',
                        '-'
                };
                break;

            case 'V':
                morseCodeSignals = new char[] {
                        '.',
                        '.',
                        '.',
                        '-'
                };
                break;

            case 'W':
                morseCodeSignals = new char[] {
                        '.',
                        '-',
                        '-'
                };
                break;

            case 'X':
                morseCodeSignals = new char[] {
                        '-',
                        '.',
                        '.',
                        '-'
                };
                break;

            case 'Y':
                morseCodeSignals = new char[] {
                        '-',
                        '.',
                        '-',
                        '-'
                };
                break;

            case 'Z':
                morseCodeSignals = new char[] {
                        '-',
                        '-',
                        '.',
                        '.'
                };
                break;

            default:
                System.out.println("Unmatched Character");
                break;
        }
        StringBuilder morseCodeString = new StringBuilder();
        for(char morseCodeSignal : morseCodeSignals) {
            morseCodeString.append(morseCodeSignal);
        }
        return morseCodeString.toString();
    }

}

