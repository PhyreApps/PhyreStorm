package org.phyreapps.phyrestorm

import com.intellij.codeInspection.*
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiRecursiveElementVisitor
import com.jetbrains.php.lang.psi.elements.If
import org.jetbrains.annotations.Nls


import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.openapi.diagnostic.Logger

class NestedIfInspection : LocalInspectionTool() {

    private val log = Logger.getInstance(NestedIfInspection::class.java)

    private val maxIfDept = 3;

    override fun getShortName(): String {
        return "NestedIfInspection"
    }
    override fun getDisplayName(): String {
        return "Non-optimized arrays mapping"
    }

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PsiRecursiveElementVisitor() {
            override fun visitElement(element: PsiElement) {
                // Check if the element is an If statement
                if (element is If) {
                    val (nestingDepth) = getNestingDepth(element)
                    if (nestingDepth >= maxIfDept) {
                        log.info("Too many nested if statements detected at depth $nestingDepth: ${element.text}")
                        holder.registerProblem(
                            element,
                            "Too many nested if statements (depth: $nestingDepth)",
                            ProblemHighlightType.ERROR
                        )
                        return
                    }
                }
                super.visitElement(element)
            }
        }
    }


    private fun getNestingDepth(ifStatement: If): Pair<Int, PsiElement?> {
        var depth = 0
        var parent = ifStatement.parent
        while (parent != null && depth < maxIfDept) { // Add a safe upper limit
            if (parent is If) {
                depth++
            }
            parent = parent.parent
        }
        return Pair(depth, parent)
    }

}
