package org.phyreapps.phyrestorm


import com.intellij.codeInspection.*
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor
import org.jetbrains.annotations.NotNull
import com.jetbrains.php.lang.psi.elements.Function

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
                            ProblemHighlightType.ERROR
                        )
                    }
                }
            }

            // Helper method to check if the function has a base case
            fun hasBaseCase(function: Function): Boolean {
                // Check if there's a base case (e.g., an if condition that halts the recursion)
                val bodyText: String = function.getText()
                return bodyText.contains("if") && bodyText.contains("<=")
            }
        }
    }
}
