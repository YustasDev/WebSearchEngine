package services;

import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;
import org.javatuples.Pair;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Morphology {

    //private static Map<List<String>, Integer> lemmMap = new LinkedHashMap<>();
    private static Map<String, Integer> normalizedLemmMap;

    public static void main(String[] args) throws IOException {

       // Pair<Map<String, Integer>, String> lemTextMap = getSetLemmas("Пока мама мыла раму, Петя помыл ванну с мылом.");
        //Map<String, Integer> lemTextMap = getSetLemmas("ученые нашли новое месторождение полезных ископаемых, молодцы ученые, хотя и не герои");
        //Map<String, Integer> lemTextMap = getSetLemmas("Повторное появление леопарда в Осетии позволяет предположить, что леопард постоянно обитает в некоторых районах Северного Кавказа.");
        //Pair<Map<String, Integer>, String> lemTextMap  = getSetLemmas("Вася и Петя пошли в лес, а потом в поле; лишь Саша не пошел - но он почти герой");
        Pair<Map<String, Integer>, String> lemTextMap  = getSetLemmas("Мы и они пониженной толщиной электроники");
        System.out.println(lemTextMap);

    }

    public static Pair<Map<String, Integer>, String> getSetLemmas(String text) throws IOException {
        normalizedLemmMap = new LinkedHashMap<>();
        //RussianAnalyzer analyzer = new RussianAnalyzer();
        LuceneMorphology luceneMorph = new RussianLuceneMorphology();
        Set<String> treatedWords = new HashSet<>();  // store the processed words so you don't get ==> word/word/word ...
       // String textWithLemmas = text.toLowerCase();
        StringBuilder stringBuilder = new StringBuilder();
        String[] disassembledText = text.trim().split("(\\s+)|(?=[А-Я]{1,})");
        for (String str : disassembledText) {
            //String s = str.toLowerCase().replaceAll("[\\p{Punct}\\s&&[^\\h]&&[^-]]", "");
            String s = str.toLowerCase().replaceAll("[\\p{Punct}\\s&&[^\\h]&&[^-]]", "");
            if (!(s == null || s.isEmpty() || s.trim().isEmpty())) {
                List<String> wordBaseForms = luceneMorph.getMorphInfo(s);
                String selectedWord = wordChoice(s, wordBaseForms);
                List<String> wordBaseFormsOneMore = luceneMorph.getMorphInfo(selectedWord);
                String word = wordBaseFormsOneMore.get(0);

                //System.out.println(wordBaseForms);
                //for (String word : wordBaseForms) {
                //String word = wordBaseForms.get(0);

                    if (!(word.contains("СОЮЗ") || word.contains("МЕЖД") || word.contains("ПРЕДЛ") || word.contains("МС") || selectedWord.length()<=2)) {
                        List<String> lemmaForms = luceneMorph.getNormalForms(s);
                        String needWord = wordChoice(selectedWord, lemmaForms);
                        Integer countlemm = normalizedLemmMap.get(needWord);
      // ====================================add a lemma to each word (for 'lemmatized_content') ==============================>
                          String s_withLemma = s + "|" + needWord + " ";
                          stringBuilder.append(s_withLemma);

                        /*     if(!treatedWords.contains(s)) {
                            int startPointSplit = textWithLemmas.indexOf(s);
                            if (startPointSplit > -1){
                            String subText1 = textWithLemmas.substring(0, startPointSplit);
                            String subText2 = textWithLemmas.substring(startPointSplit);
                            String replacementThis = s + "|" + needWord;
                            subText2 = subText2.replace(s, replacementThis);
                            textWithLemmas = subText1 + subText2;
                            treatedWords.add(s);
                            treatedWords.add(needWord);
                         }
                        }   */
      //===========================================================================================<

                        if (countlemm == null) {
                            normalizedLemmMap.put(needWord, 1);
                            continue;
                        } else {
                            countlemm++;
                            normalizedLemmMap.replace(needWord, countlemm);
                        }
                    }
                }
            }
        String textWithLemmas = stringBuilder.toString();
        Pair<Map<String, Integer>, String> processedText = new Pair<Map<String, Integer>, String>(normalizedLemmMap, textWithLemmas);
        return processedText;
    }


    public static String wordChoice (String original, List<String> lemmsList){
        String minimalWord = lemmsList.get(0);
        int minimum = 100; // a knowingly large size of distance
        for (String word : lemmsList) {

            String[] wordArray = word.split("\\|");
            word = wordArray[0];

            int distance = levenstain(original, word);
            if (distance <= minimum) {
                    minimum = distance;
                    minimalWord = word;
                }
            }
        return minimalWord;
        }


    public static int levenstain(String str1, String str2) {
        // see the Levenshtein distance algorithm
        int[] Di_1 = new int[str2.length() + 1];
        int[] Di = new int[str2.length() + 1];

        for (int j = 0; j <= str2.length(); j++) {
            Di[j] = j; // (i == 0)
        }

        for (int i = 1; i <= str1.length(); i++) {
            System.arraycopy(Di, 0, Di_1, 0, Di_1.length);

            Di[0] = i; // (j == 0)
            for (int j = 1; j <= str2.length(); j++) {
                int cost = (str1.charAt(i - 1) != str2.charAt(j - 1)) ? 1 : 0;
                Di[j] = min(
                        Di_1[j] + 1,
                        Di[j - 1] + 1,
                        Di_1[j - 1] + cost
                );
            }
        }
        return Di[Di.length - 1];
    }

    private static int min(int n1, int n2, int n3) {
        return Math.min(Math.min(n1, n2), n3);
    }

}
