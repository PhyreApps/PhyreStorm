package org.phyreapps.phyrestorm


import com.intellij.codeInspection.*
import com.jetbrains.php.lang.psi.elements.Function
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor
import org.jetbrains.annotations.NotNull


class InfiniteRecursionInspection : LocalInspectionTool() {


    override fun getShortName(): String {
        return "InfiniteRecursionInspection"
    }
    override fun getDisplayName(): String {
        return "Non-optimized arrays mapping"
    }


    @NotNull
    override fun buildVisitor(@NotNull holder: ProblemsHolder, isOnTheFly: Boolean): PhpElementVisitor {
        return object : PhpElementVisitor() {
            override fun visitPhpFunction(function: Function) {
                super.visitPhpFunction(function)

                // Check if the function calls itself recursively
                if (function.text.contains(function.name)) {
                    // Check if there is a base case to stop recursion
                    if (!hasBaseCase(function)) {
                        holder.registerProblem(
                            function,
                            "Recursive function detected without a base case. This may cause infinite recursion.",
                            ProblemHighlightType.WARNING
                        )
                    }
                }
            }

            // Helper method to check if the function has a base case
            private fun hasBaseCase(function: Function): Boolean {
                val bodyText: String = function.getText()

                // Check if there's a return statement or an exit statement in the function (common for stopping recursion)
                if (bodyText.contains("return") || bodyText.contains("exit") || bodyText.contains("die")) {
                    return true
                }

                // If no base case is found, return false
                return false
            }
        }
    }
}
