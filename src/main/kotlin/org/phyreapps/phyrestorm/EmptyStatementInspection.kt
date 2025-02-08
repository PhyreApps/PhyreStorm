package org.phyreapps.phyrestorm

import com.intellij.codeInspection.*
import com.jetbrains.php.lang.psi.elements.Variable
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor
import org.jetbrains.annotations.NotNull


class EmptyStatementInspection : LocalInspectionTool() {

    override fun getShortName(): String {
        return "EmptyStatementInspection"
    }

    override fun getDisplayName(): String {
        return "Empty Statements"
    }

    @NotNull
    override fun buildVisitor(@NotNull holder: ProblemsHolder, isOnTheFly: Boolean): PhpElementVisitor {
        return object : PhpElementVisitor() {
            override fun visitPhpVariable(variable: Variable) {
                super.visitPhpVariable(variable)

                // Check if the variable is used before being defined (simple check for null for demo purposes)
//                if (!variable.isReferenceTo(null)) {
//                    var variableName = variable.name;
//                    holder.registerProblem(
//                        variable,
//                        "Variable name '$variableName' is undefined",
//                        ProblemHighlightType.ERROR,
//                        *emptyArray()
//                    )
//                }
            }
        }
    }

}
