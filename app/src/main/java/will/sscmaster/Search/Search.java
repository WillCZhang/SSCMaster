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
        // initialize all elements in temp array to 0
        int[] temp = new int[list.size()];
        for (int i = 0; i < temp.length; i++)
            temp[i] = 0;
        // while `aim` is not empty
        //      let currChar = aim.charAt(0)
        //      for each string in `list`
        //          if current element contains currChar:
        //              if index of currChar at the remaining string >= previous index in temp[string pos]
        //                  update temp[string pos]
        //                  remove currChar from current element
        //              else
        //                  add curr to toRemove list
        //          else
        //              add curr to toRemove list
        //      removeAll(toRemove)
        //      remove currChar from`aim`
        // end while

        // old implementation:
//        while (aim.length() != 0) {
//            char firstChar = aim.charAt(0);
//            for (int stringIndex = 0; stringIndex < fakeList.size(); stringIndex++) {
//                String currString = fakeList.get(stringIndex);
//                if (currString.contains(firstChar + "")) {
//                    if (currString.indexOf(firstChar) >= temp[stringIndex]) {
//                        temp[stringIndex] = currString.indexOf(firstChar);
//                        replaceString(fakeList, firstChar, stringIndex, currString);
//                    } else
//                        toRemove(temp, stringIndex, currString);
//                } else
//                    toRemove(temp, stringIndex, currString);
//            }
//            removeList(fakeList);
//            aim = removeFirstChar(aim, firstChar+"");
//        }

        // new implementation that each iteration based on string list instead of the input `aim`
        for (int stringIndex = 0; stringIndex < list.size(); stringIndex++) {
            String currString = list.get(stringIndex).toLowerCase().trim().replaceAll("\\W", "");
            String fakeAim = aim.toLowerCase().trim().replaceAll("\\W", "");
            while (fakeAim.length() != 0) {
                char firstChar = fakeAim.charAt(0);
                String charString = firstChar + "";
                if (currString.contains(charString)) {
                    if (currString.substring(temp[stringIndex]).contains(charString)) {
                        temp[stringIndex] = currString.indexOf(firstChar);
                        currString = removeFirstChar(currString, charString);
                    } else {
                        toRemove.add(list.get(stringIndex));
                        break;
                    }
                } else {
                    toRemove.add(list.get(stringIndex));
                    break;
                }
                fakeAim = removeFirstChar(fakeAim, charString);
            }
        }
        removeList(list);
    }

    private static String removeFirstChar(String aim, String regex) {
        return aim.replaceFirst(regex, "");
    }

    private static void removeList(List<String> list) {
        list.removeAll(toRemove);
        toRemove.clear();
    }
}
