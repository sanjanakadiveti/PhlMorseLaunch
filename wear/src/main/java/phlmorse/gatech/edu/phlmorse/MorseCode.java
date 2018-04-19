package phlmorse.gatech.edu.phlmorse;

/**
 * Created by sanjanakadiveti on 4/3/18.
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MorseCode {

    //////////////////////////////////////////////////////////////
    // Static Variables

    public enum MorseCodeSignal {
        DOT, DASH
    }

    public static final int SPACE_MS = 100;

    //////////////////////////////////////////////////////////////
    // Properties/Attributes

    private String str; // Letter/this.str/Sentence
    private int INITIAL_WAIT_MS = 1000;
    private int PAUSE_SIGNALS_MS = 500;
    private int PAUSE_LETTERS_MS = 1500;
    private int PAUSE_WORDS_MS = 5000;
    private int DOT_MS = 250;
    private int DASH_MS = 750;

    //////////////////////////////////////////////////////////////
    // Constructor

    public MorseCode(String str) {
        this.str = str;
    }

    //////////////////////////////////////////////////////////////
    // Public Methods

    public MorseCodeSignal[][] getSignal() {
        MorseCodeSignal[][] morseCodeSignals = new MorseCodeSignal[this.str.length()][];
        for (int i = 0; i < this.str.length(); i++) {
            char c = this.str.charAt(i);
            morseCodeSignals[i] = getSignalArray(c);
        }
        return morseCodeSignals;
    }


    // Convert MorseCodeSignals to VibrationTime
    public long[][] getVibrationTime() {
        MorseCodeSignal[][] morseCodeSignals = getSignal();
        long[][] vibrationTimes = new long[this.str.length()][];
        for (int i = 0; i < morseCodeSignals.length; i++) {
            vibrationTimes[i] = new long[morseCodeSignals[i].length];
            for (int j = 0; j < morseCodeSignals[i].length; j++) {
                vibrationTimes[i][j] = getVibrationTimeArray(morseCodeSignals[i][j]);
            }
        }
        return vibrationTimes;
    }

    public long[] getVibrationTimeFlattened() {
        long[][] vibrationTimes = getVibrationTime();
        List<Long> vibrationTimeList = new ArrayList<Long>();
        for (int i = 0; i < vibrationTimes.length; i++) {
            for (int j = 0; j < vibrationTimes[i].length; j++) {
                vibrationTimeList.add(vibrationTimes[i][j]);
            }
        }
        // Convert Long[] to long[]
        Long[] longObjectArray = vibrationTimeList.toArray(new Long[vibrationTimeList.size()]);
        long[] longArray = longObjectToLongPrimitive(longObjectArray);

        return longArray;
    }

    public long[] getVibrationPatternTimes() {
        long[] longArray = getVibrationTimeFlattened();

        // Manipulate array, insert initial value and weave in off values between the given on values
        int nextCharacterIndex = 0;
        long[] returnArray = new long[1 + (longArray.length * 2)];

        for (int i = 0; i < returnArray.length; i++) {
            if (i == 0) {
                returnArray[i] = INITIAL_WAIT_MS;
            } else if(i == returnArray.length - 1) {
                returnArray[i] = PAUSE_WORDS_MS;
            } else if(isLetterPause(i)) {
                returnArray[i] = PAUSE_LETTERS_MS;
            } else if (i % 2 == 0) {
                returnArray[i] = PAUSE_SIGNALS_MS; // vibration off
            } else if (i % 2 == 1) {
                returnArray[i] = longArray[nextCharacterIndex]; // vibration on
                nextCharacterIndex++;
            }
        }
        return returnArray;
    }

    private boolean isLetterPause(int i) {
        int[] intervals = new int[str.length()];
        int signals = 0;
        for(int j = 0; j < intervals.length; j++) {
            signals += getSignalArray(str.charAt(j)).length * 2;
            intervals[j] = signals;
        }
        for(int j = 0; j < intervals.length; j++) {
            if(i == intervals[j]) return true;
        }
        return false;
    }

    public long[] getVibrationPatternTimesForXMinutes(int minutes) {
        int millisecondsInXMinutes = minutes * 60 * 1000; // minutes * seconds * milliseconds
        long[] patternTimesForOneSequence = getVibrationPatternTimes();
        long[] patternTimesForOneSequenceWithoutInitialWaitTime = Arrays.copyOfRange(patternTimesForOneSequence, 1, patternTimesForOneSequence.length);

        // Calculate how many copies need to be made
        long sum = getSum(patternTimesForOneSequence);
        int repetitions = millisecondsInXMinutes / (int)sum;

        long[] patternTimes = Arrays.copyOf(patternTimesForOneSequence, patternTimesForOneSequence.length); // account for initial wait time
        for (int i = 0; i < repetitions; i++) {
            patternTimes = concat(patternTimes, patternTimesForOneSequenceWithoutInitialWaitTime);
        }
        return patternTimes;
    }

    //////////////////////////////////////////////////////////////
    // Private Utility Methods

    public long[] longObjectToLongPrimitive(Long[] longObjectArray) {
        long[] longArray = new long[longObjectArray.length];
        for(int i = 0; i < longObjectArray.length; i++) {
            longArray[i] = (long) longObjectArray[i];
        }
        return longArray;
    }

    private static long getSum(long[] nums) {
        long sum = 0;
        for (long i : nums) {
            sum += i;
        }
        return sum;
    }

    public long[] concat(long[] a, long[] b) {
        int aLen = a.length;
        int bLen = b.length;
        long[] c= new long[aLen+bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }

    //////////////////////////////////////////////////////////////
    // Private Helper Methods

    private MorseCodeSignal[] getSignalArray(char c) {
        c = Character.toUpperCase(c);
        MorseCodeSignal[] morseCodeSignals = null;
        switch (c) {
            case 'A':
                morseCodeSignals = new MorseCodeSignal[] {
                        MorseCodeSignal.DOT,
                        MorseCodeSignal.DASH
                };
                break;

            case 'B':
                morseCodeSignals = new MorseCodeSignal[] {
                        MorseCodeSignal.DASH,
                        MorseCodeSignal.DOT,
                        MorseCodeSignal.DOT,
                        MorseCodeSignal.DOT
                };
                break;

            case 'C':
                morseCodeSignals = new MorseCodeSignal[] {
                        MorseCodeSignal.DASH,
                        MorseCodeSignal.DOT,
                        MorseCodeSignal.DASH,
                        MorseCodeSignal.DOT
                };
                break;

            case 'D':
                morseCodeSignals = new MorseCodeSignal[] {
                        MorseCodeSignal.DASH,
                        MorseCodeSignal.DOT,
                        MorseCodeSignal.DOT
                };
                break;

            case 'E':
                morseCodeSignals = new MorseCodeSignal[] {
                        MorseCodeSignal.DOT
                };
                break;

            case 'F':
                morseCodeSignals = new MorseCodeSignal[] {
                        MorseCodeSignal.DOT,
                        MorseCodeSignal.DOT,
                        MorseCodeSignal.DASH,
                        MorseCodeSignal.DOT
                };
                break;

            case 'G':
                morseCodeSignals = new MorseCodeSignal[] {
                        MorseCodeSignal.DASH,
                        MorseCodeSignal.DASH,
                        MorseCodeSignal.DOT
                };
                break;

            case 'H':
                morseCodeSignals = new MorseCodeSignal[] {
                        MorseCodeSignal.DOT,
                        MorseCodeSignal.DOT,
                        MorseCodeSignal.DOT,
                        MorseCodeSignal.DOT
                };
                break;

            case 'I':
                morseCodeSignals = new MorseCodeSignal[] {
                        MorseCodeSignal.DOT,
                        MorseCodeSignal.DOT
                };
                break;

            case 'J':
                morseCodeSignals = new MorseCodeSignal[] {
                        MorseCodeSignal.DOT,
                        MorseCodeSignal.DASH,
                        MorseCodeSignal.DASH,
                        MorseCodeSignal.DASH
                };
                break;

            case 'K':
                morseCodeSignals = new MorseCodeSignal[] {
                        MorseCodeSignal.DASH,
                        MorseCodeSignal.DOT,
                        MorseCodeSignal.DASH
                };

            case 'L':
                morseCodeSignals = new MorseCodeSignal[] {
                        MorseCodeSignal.DOT,
                        MorseCodeSignal.DASH,
                        MorseCodeSignal.DOT,
                        MorseCodeSignal.DOT
                };
                break;

            case 'M':
                morseCodeSignals = new MorseCodeSignal[] {
                        MorseCodeSignal.DASH,
                        MorseCodeSignal.DASH
                };
                break;

            case 'N':
                morseCodeSignals = new MorseCodeSignal[] {
                        MorseCodeSignal.DASH,
                        MorseCodeSignal.DOT
                };
                break;

            case 'O':
                morseCodeSignals = new MorseCodeSignal[] {
                        MorseCodeSignal.DASH,
                        MorseCodeSignal.DASH,
                        MorseCodeSignal.DASH
                };
                break;

            case 'P':
                morseCodeSignals = new MorseCodeSignal[] {
                        MorseCodeSignal.DOT,
                        MorseCodeSignal.DASH,
                        MorseCodeSignal.DASH,
                        MorseCodeSignal.DOT
                };
                break;

            case 'Q':
                morseCodeSignals = new MorseCodeSignal[] {
                        MorseCodeSignal.DASH,
                        MorseCodeSignal.DASH,
                        MorseCodeSignal.DOT,
                        MorseCodeSignal.DASH
                };
                break;

            case 'R':
                morseCodeSignals = new MorseCodeSignal[] {
                        MorseCodeSignal.DOT,
                        MorseCodeSignal.DASH,
                        MorseCodeSignal.DOT
                };
                break;

            case 'S':
                morseCodeSignals = new MorseCodeSignal[] {
                        MorseCodeSignal.DOT,
                        MorseCodeSignal.DOT,
                        MorseCodeSignal.DOT
                };
                break;

            case 'T':
                morseCodeSignals = new MorseCodeSignal[] {
                        MorseCodeSignal.DASH
                };
                break;

            case 'U':
                morseCodeSignals = new MorseCodeSignal[] {
                        MorseCodeSignal.DOT,
                        MorseCodeSignal.DOT,
                        MorseCodeSignal.DASH
                };
                break;

            case 'V':
                morseCodeSignals = new MorseCodeSignal[] {
                        MorseCodeSignal.DOT,
                        MorseCodeSignal.DOT,
                        MorseCodeSignal.DOT,
                        MorseCodeSignal.DASH
                };
                break;

            case 'W':
                morseCodeSignals = new MorseCodeSignal[] {
                        MorseCodeSignal.DOT,
                        MorseCodeSignal.DASH,
                        MorseCodeSignal.DASH
                };
                break;

            case 'X':
                morseCodeSignals = new MorseCodeSignal[] {
                        MorseCodeSignal.DASH,
                        MorseCodeSignal.DOT,
                        MorseCodeSignal.DOT,
                        MorseCodeSignal.DASH
                };
                break;

            case 'Y':
                morseCodeSignals = new MorseCodeSignal[] {
                        MorseCodeSignal.DASH,
                        MorseCodeSignal.DOT,
                        MorseCodeSignal.DASH,
                        MorseCodeSignal.DASH
                };
                break;

            case 'Z':
                morseCodeSignals = new MorseCodeSignal[] {
                        MorseCodeSignal.DASH,
                        MorseCodeSignal.DASH,
                        MorseCodeSignal.DOT,
                        MorseCodeSignal.DOT
                };
                break;

            default:
                System.out.println("Unmatched Character");
                break;
        }
        return morseCodeSignals;
    }

    private int getVibrationTimeArray(MorseCodeSignal morseCodeSignal) {
        int time = 0;
        switch (morseCodeSignal) {
            case DOT:
                time = DOT_MS;
                break;

            case DASH:
                time = DASH_MS;
                break;

            default:
                break;
        }
        return time;
    }
}