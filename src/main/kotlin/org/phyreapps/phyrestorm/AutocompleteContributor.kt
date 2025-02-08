package org.phyreapps.phyrestorm

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext
import com.jetbrains.php.lang.psi.PhpFile
import com.jetbrains.php.lang.psi.elements.PhpClass
import com.jetbrains.php.lang.psi.elements.PhpNamedElement

class AutocompleteContributor : CompletionContributor() {

    init {
        // Register completion for PHP language in a specific context
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement().inside(PhpFile::class.java),
            PhpCompletionProvider()
        )
    }



    private class PhpCompletionProvider : CompletionProvider<CompletionParameters>() {

        override fun addCompletions(
            parameters: CompletionParameters,
            context: ProcessingContext,
            result: CompletionResultSet
        ) {
            val position = parameters.position

            result.addElement(LookupElementBuilder.create("BOZHIDAR"))
            result.addLookupAdvertisement("This is a sample advertisement for PHP completion")
            
        }
    }
}