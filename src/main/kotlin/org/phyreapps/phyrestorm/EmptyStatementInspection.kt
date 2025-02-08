package org.phyreapps.phyrestorm

import com.intellij.codeInspection.*
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiRecursiveElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.php.lang.psi.elements.Statement
import com.jetbrains.php.lang.psi.elements.PhpExpression
import org.jetbrains.annotations.Nls

class EmptyStatementInspection : LocalInspectionTool() {

    override fun getShortName(): String {
        return "EmptyStatementInspection"
    }

    override fun getDisplayName(): String {
        return "Empty Statements"
    }

    
}
