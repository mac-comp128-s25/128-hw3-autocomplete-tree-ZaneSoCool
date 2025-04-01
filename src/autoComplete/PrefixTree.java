package autoComplete;

import java.util.ArrayList;
import java.util.Map;

/**
 * A prefix tree used for autocompletion. The root of the tree just stores links to child nodes (up to 26, one per letter).
 * Each child node represents a letter. A path from a root's child node down to a node where isWord is true represents the sequence
 * of characters in a word.
 */
public class PrefixTree {
    private TreeNode root; 

    // Number of words contained in the tree
    private int size;

    public PrefixTree(){
        root = new TreeNode();
    }

    /**
     * Adds the word to the tree where each letter in sequence is added as a node
     * If the word, is already in the tree, then this has no effect.
     * @param word
     */
    public void add(String word){
        char[] characters = word.toCharArray();
        TreeNode current = root;

        if(contains(word)){
            return;
        }

        for (int i = 0 ; i < characters.length ; i++) {
            char c = characters[i];
            
            if (!current.children.containsKey(c)){ //add new letter

                TreeNode newChar = new TreeNode();
                newChar.letter = c;

                if(i == characters.length - 1){
                    newChar.isWord = true;
                    size++;
                }

                current.children.put(c, newChar);
                current = newChar;

            } else {

                if(i == characters.length - 1){
                    current.isWord = true;
                    size++;

                } else {
                    current = current.children.get(c);
                } 
            }
        }
    }

    /**
     * Checks whether the word has been added to the tree
     * @param word
     * @return true if contained in the tree.
     */
    public boolean contains(String word){
        char[] characters = word.toCharArray();
        TreeNode current = root;

        for (int i = 0 ; i < characters.length ; i++) {
            char c = characters[i];
            
            if(current.children.containsKey(c)){
                current = current.children.get(c);
            } else{
                return false;
            }

            if(i == characters.length - 1 && current.isWord){ //at end of word
                return true;
            }

        }

        return false;
    }

    /**
     * Finds the words in the tree that start with prefix (including prefix if it is a word itself).
     * The order of the list can be arbitrary.
     * @param prefix
     * @return list of words with prefix
     */
    public ArrayList<String> getWordsForPrefix(String prefix){
        ArrayList<String> wordsWithPrefix = new ArrayList<>();

        char[] characters = prefix.toCharArray();
        TreeNode current = root;
        int index = 0;

        while(index < characters.length &&  current.children.containsKey(characters[index])){ //builds the prefix
            current = current.children.get(characters[index]);
            index++;
        }

        recursiveWordFinder(current, wordsWithPrefix, prefix);

        return wordsWithPrefix;
    }

    private void recursiveWordFinder(TreeNode current, ArrayList<String> wordsWithPrefix, String builtWord){
    
        if (current.isWord){ //checks if current word is valid
            wordsWithPrefix.add(builtWord);
        }
        
        for (Map.Entry<Character, TreeNode> entry : current.children.entrySet()) { //happens for each TreeNode's children

            String builtWordCopy = new String(builtWord); //create a copy of the current word in String format
            builtWordCopy = builtWordCopy + entry.getKey(); //add the next character to the aformentioned word

            recursiveWordFinder(entry.getValue(), wordsWithPrefix, builtWordCopy); //recursively calls itself
        }
    }

    /**
     * @return the number of words in the tree
     */
    public int size(){
        return size;
    }
    
}
