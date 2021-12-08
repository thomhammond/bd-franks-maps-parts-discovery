package com.amazon.ata.maps.partsdiscovery;

import java.util.*;

/**
 * Helps expose key words from new editions of part catalogs.
 */
public class DevicePartDiscovery {

    // --- Part A ---
    /**
     * Calculate how often each word appears in a Catalog.
     * @param catalog The catalog to calculate word frequencies for.
     * @return A Map of words that appear in the catalog to the number of times they appear.
     */
    public Map<String, Integer> calculateWordCounts(PartCatalog catalog) {
        Map<String, Integer> wordCounts = new HashMap<>();
        List<String> catalogWords = catalog.getCatalogWords();

        for (String word : catalogWords) {
            wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
        }

        return wordCounts;
    }

    // --- Part B ---
    /**
     * Removes a word from the provided word count map.
     * @param word the word to be removed
     * @param wordCounts the map to remove the word from
     */
    public void removeWord(String word, Map<String, Integer> wordCounts) {
        wordCounts.remove(word);
    }

    // --- Part C ---
    /**
     * Find the word that appears most frequently based on the word counts from a catalog.
     * @param wordCounts an association between a word and the total number of times it appeared in a catalog
     * @return The word that appears most frequently in the catalog to the number of times they appear.
     */
    public String getMostFrequentWord(Map<String, Integer> wordCounts) {
        if (wordCounts == null || wordCounts.isEmpty()) {
            return null;
        }

        int maxCount = Collections.max(wordCounts.values());
        for (String key : wordCounts.keySet()) {
            if (wordCounts.get(key) == maxCount) {
                return key;
            }
        }
        return null;
    }

    // --- Part D ---
    /**
     * Calculates the TF-IDF score for each word in a catalog. The TF-IDF score for a word
     * is equal to the count * idf score. You can assume there will be an idfScore for each word
     * in wordCounts.
     * @param wordCounts - associates a count for each word from a catalog
     * @param idfScores - associates an IDF score for each word in the catalog
     * @return a map associating each word with its TF-IDF score.
     */
    public Map<String, Double> getTfIdfScores(Map<String, Integer> wordCounts, Map<String, Double> idfScores) {
        Map<String, Double> tfIdScores = new HashMap<>();
        for (String word : wordCounts.keySet()) {
            tfIdScores.put(word, wordCounts.get(word) * idfScores.get(word));
        }
        return tfIdScores;
    }

    // --- Extension 1 ---
    /**
     * Gets the 10 highest (TF-IDF) scored words for a catalog.
     *
     * @param tfIdfScores - associates a TF-IDF score for each word in a catalog
     * @return a list of the 10 highest scored words for a catalog.
     */
    public List<String> getBestScoredWords(Map<String, Double> tfIdfScores) {
        // Create a sortable List of Entries in tfIdfScores
        List<Map.Entry<String, Double>> entries = new ArrayList<>(tfIdfScores.entrySet());

        // Comparator to sort in descending order
        Comparator<Map.Entry<String, Double>> comparator = (o1, o2) -> o2.getValue().compareTo(o1.getValue());
        entries.sort(comparator);

        // Select 10 highest scoring entries
        entries = entries.subList(0, 10);

        // Format for return
        List<String> bestScoredWords = new ArrayList<>();
        for (Map.Entry<String, Double> entry : entries) {
            bestScoredWords.add(entry.getKey());
        }
        return bestScoredWords;
    }

    // --- Extension 2 ---
    /**
     * Calculates the IDF score for each word in a set of catalogs. The IDF score for a word
     * is equal to the inverse of the total number of times appears in all catalogs.
     * Assume df is the sum of the counts of a single word across all catalogs, then idf = 1.0/df.
     *
     * @param catalogWordCounts - a list of maps that associate a count for each word for each catalog
     * @return a map associating each word with its IDF score.
     */
    public Map<String, Double> calculateIdfScores(List<Map<String,Integer>> catalogWordCounts) {
        int N = catalogWordCounts.size();
        Map<String, Integer> DF = new HashMap<>();

        for (Map<String, Integer> catalog : catalogWordCounts) {
            for (String word : catalog.keySet()) {
                DF.put(word, DF.getOrDefault(word, 0) + 1);
            }
        }

        Map<String, Double> idfScores = new HashMap<>();
        for (String word : DF.keySet()) {
            double idf = (double) N / DF.get(word);
            idfScores.put(word, Math.log10(idf));
        }
        return idfScores;
    }

}
