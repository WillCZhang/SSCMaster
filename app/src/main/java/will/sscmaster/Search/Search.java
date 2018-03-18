package will.sscmaster.Search;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Will on 2018/3/17.
 */

public class Search {
    private static List<String> toRemove = new ArrayList<>();

    // Note: the input list will be modified!
    public static void searchGivenString(String aim, List<String> list) {
        // My algorithm works as follow:
        // the indices of the temp array is the corresponding indices in the given `list`
        // initialize all elements in temp array to -1
        int[] temp = new int[list.size()];
        for (int i = 0; i < temp.length; i++)
            temp[i] = -1;
        List<String> fakeList = new ArrayList<>(list);
        // while `aim` is not empty
        //      let currChar = aim.charAt(0)
        //      for each string in `list`
        //          if current element contains currChar:
        //              if index of currChar at the remaining string >= previous index in temp[string pos]
        //                  update temp[string pos]
        //                  remove currChar from current element
        //              else
        //                  add curr to toRemove list
        //                  set temp[string pos] = -1
        //          else
        //              add curr to toRemove list
        //              set temp[string pos] = -1
        //      removeAll(toRemove)
        //      remove currChar from`aim`
        // end while
        // return result list based on temp array
        while (aim.length() != 0) {
            char firstChar = aim.charAt(0);
            for (int stringIndex = 0; stringIndex < fakeList.size(); stringIndex++) {
                String currString = fakeList.get(stringIndex);
                if (currString.contains(firstChar + "")) {
                    if (currString.indexOf(firstChar) >= temp[stringIndex]) {
                        temp[stringIndex] = currString.indexOf(firstChar);
                        replaceString(fakeList, firstChar, stringIndex, currString);
                    } else
                        toRemove(temp, stringIndex, currString);
                } else
                    toRemove(temp, stringIndex, currString);
            }
            removeList(fakeList);
            aim = removeFirstChar(aim, firstChar+"");
        }
    }

    private static void replaceString(List<String> list, char firstChar, int stringIndex, String currString) {
        String tempString = removeFirstChar(currString,firstChar+"");
        list.remove(stringIndex);
        list.add(stringIndex, tempString);
    }

    private static void toRemove(int[] temp, int stringIndex, String currString) {
        toRemove.add(currString);
        temp[stringIndex] = -1;
    }

    private static String removeFirstChar(String aim, String regex) {
        return aim.replaceFirst(regex, "");
    }

    private static void removeList(List<String> list) {
        list.removeAll(toRemove);
        toRemove.clear();
    }
}
