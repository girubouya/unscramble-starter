package com.example.unscramble.ui

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import com.example.unscramble.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.update


class GameViewModel:ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    var userGuess by mutableStateOf("")
        private set

    private lateinit var currentWord: String
    private var usedWords:MutableSet<String> = mutableSetOf()

    init {
        resetGame()
    }

    fun updateUserGuess(guessWord:String){
        userGuess = guessWord
    }

    private fun shuffleCurrentWord(word:String): String{
        val tempWord = word.toCharArray()
        tempWord.shuffle()
        while(String(tempWord).equals(word)){
            tempWord.shuffle()
        }
        return String(tempWord)
    }

    private fun pickRandomWordAndShuffle():String{
        currentWord = allWords.random()
        if(usedWords.contains(currentWord)){
            return pickRandomWordAndShuffle()
        }else{
            usedWords.add(currentWord)
            return shuffleCurrentWord(currentWord)
        }
    }
    fun resetGame(){
        usedWords.clear()
        _uiState.value = GameUiState(currentScrambledWord = pickRandomWordAndShuffle())
    }

    fun checkUserGuess(){
        if(userGuess.equals(currentWord,ignoreCase = true)){
            _uiState.update { currenState ->
                currenState.copy(isGuessedWordWrong = false)
            }
        }else{
            _uiState.update { currentState ->
                currentState.copy(isGuessedWordWrong = true)
            }
        }
        updateUserGuess("")
    }
}
