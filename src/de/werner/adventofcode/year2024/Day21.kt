package de.werner.adventofcode.year2024

import de.werner.adventofcode.common.*

class Day21 {
    private val testInput = readInput("""2024\Day21_test""")
    private val input = readInput("""2024\Day21""")

    private val codePad = mapOf(
        'A' to mapOf('0' to "<", '3' to "^"),
        '0' to mapOf('2' to "^", 'A' to ">"),
        '1' to mapOf('4' to "^", '2' to ">"),
        '2' to mapOf('1' to "<", '0' to "v", '3' to ">", '5' to "^"),
        '3' to mapOf('2' to "<", 'A' to "v", '6' to "^"),
        '4' to mapOf('7' to "^", '5' to ">", '1' to "v"),
        '5' to mapOf('4' to "<", '8' to "^", '6' to ">", '2' to "v"),
        '6' to mapOf('5' to "<", '9' to "^", '3' to "v"),
        '7' to mapOf('4' to "v", '8' to ">"),
        '8' to mapOf('7' to "<", '5' to "v", '9' to ">"),
        '9' to mapOf('8' to "<", '6' to "v")
    )

    private val controlPad = mapOf(
        '^' to mapOf('A' to ">", 'v' to "v"),
        'A' to mapOf('^' to "<", '>' to "v"),
        '>' to mapOf('A' to "^", 'v' to "<"),
        'v' to mapOf('<' to "<", '^' to "^", '>' to ">"),
        '<' to mapOf('v' to ">")
    )

    fun solvePart1(input: List<String> = this.input): Long = timing {
        var complexity = 0L
        for (code in input) {
            val shortestPath = controlChain(codePad, controlPad, code)
            val pathComplexity = calculateComplexity(code, shortestPath.length)
            complexity += pathComplexity
            println("$code: $shortestPath -> L: ${shortestPath.length}, C: $pathComplexity")
        }

//        return input
//            .map { it to controlChain(codePad, controlPad, it) }
//            .sumOf { calculateComplexity(it.first, it.second.length) }
        println(complexity)
        return@timing complexity
    }

    fun testPart1() = solvePart1(testInput)

    fun solvePart2(input: List<String> = this.input): Int = timing { input.size }

    fun testPart2() = solvePart2(testInput)

    private fun findAllPaths(
        pad: Map<Char, Map<Char, String>>,
        start: Char,
        target: Char
    ): List<String> {

        val visited = mutableSetOf<Char>()
        val paths = mutableListOf<String>()

        fun dfs(current: Char, path: String) {
            if (current == target) {
                paths.add(path)
                return
            }

            visited.add(current)
            pad[current]?.forEach { (neighbor, direction) ->
                if (!visited.contains(neighbor)) {
                    dfs(neighbor, path + direction)
                }
            }
            visited.remove(current)
        }

        dfs(start, "")
        return paths
    }

    private fun findBestPath(paths: List<String>): String {
        var bestPath = ""
        var bestCost = Int.MAX_VALUE

        for (path in paths) {
            val cost = path.length + calculatePenalty(path)
            if (cost < bestCost) {
                bestCost = cost
                bestPath = path
            }
        }
        return bestPath
    }

    private fun calculatePenalty(path: String): Int = path
        .windowed(2)
        .count { it[0] != it[1] } * 2

    private fun controlRobot(
        pad: Map<Char, Map<Char, String>>,
        start: Char,
        sequence: String
    ): String {
        var currentPosition = start
        val result = StringBuilder()

        for (target in sequence) {
            val paths = findAllPaths(pad, currentPosition, target)
            val bestPath = findBestPath(paths)
            result.append(bestPath).append("A")
            currentPosition = target
        }
        return result.toString()
    }

    private fun controlChain(
        codePad: Map<Char, Map<Char, String>>,
        controlPad: Map<Char, Map<Char, String>>,
        code: String
    ): String {
        val r1Commands = controlRobot(codePad, 'A', code)
        val r2Commands = controlRobot(controlPad, 'A', r1Commands)
        val r3Commands = controlRobot(controlPad, 'A', r2Commands)
        return r3Commands
    }

    private fun calculateComplexity(code: String, shortestPathLength: Int): Long {
        val numericPart = code.takeWhile { it.isDigit() }.toLongOrNull() ?: 0L

        return shortestPathLength * numericPart
    }
}

