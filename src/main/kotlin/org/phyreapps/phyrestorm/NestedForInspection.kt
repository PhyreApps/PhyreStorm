package org.phyreapps.phyrestorm

import com.intellij.codeInspection.*
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiRecursiveElementVisitor
import com.jetbrains.php.lang.psi.elements.For
import org.jetbrains.annotations.Nls


import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.openapi.diagnostic.Logger

class NestedForInspection : LocalInspectionTool() {

    private val log = Logger.getInstance(NestedForInspection::class.java)

    private val maxIfDept = 3;

    override fun getShortName(): String {
        return "NestedForInspection"
    }
    override fun getDisplayName(): String {
        return "Non-optimized arrays mapping"
    }

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PsiElementVisitor() {
            override fun visitElement(element: PsiElement) {
                // Check if the element is an If statement
                if (element is For) {
                    val (nestingDepth) = getNestingDepth(element)
                    if (nestingDepth >= maxIfDept) {
                        log.info("Too many nested for statements detected at depth $nestingDepth: ${element.text}")
                        holder.registerProblem(
                            element,
                            "Too many nested for statements (depth: $nestingDepth)",
                            ProblemHighlightType.ERROR
                        )
                    }
                }
                super.visitElement(element)
            }
        }
    }


    private fun getNestingDepth(forStatement: For): Pair<Int, PsiElement?> {
        var depth = 0
        var parent = forStatement.parent
        while (parent != null && depth < maxIfDept) { // Add a safe upper limit
            if (parent is For) {
                depth++
            }
            parent = parent.parent
        }
        return Pair(depth, parent)
    }

}
