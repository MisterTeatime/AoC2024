package de.werner.adventofcode.year2024

import de.werner.adventofcode.common.*

class Day25 {
    private val testInput = readInput("""2024\Day25_test""")
    private val input = readInput("""2024\Day25""")

    fun solvePart1(input: List<String> = this.input): Int = timing {
        val (locks, keys) = parsePatterns(input)

        val result = locks.flatMap { l -> keys.map { k -> doesFit(l, k) }}

        return@timing result.count { it }
    }

    fun testPart1() = solvePart1(testInput)

    fun solvePart2(input: List<String> = this.input): Int = timing { input.size }

    fun testPart2() = solvePart2(testInput)

    private fun doesFit(lock: Pair<List<Int>, Int>, key: Pair<List<Int>, Int>): Boolean {
        val (lockColumns, lockSize) = lock
        val (keyColumns, keySize) = key

        if (lockSize != keySize) return false

        for (i in lockColumns.indices) {
            if (lockColumns[i] + keyColumns[i] > lockSize) return false
        }

        return true
    }

    private fun calculateColumnFill(block: List<String>): List<Int> {
        val width = block.first().length
        return (0 until width).map { col ->
            block.count { it[col] == '#' }
        }
    }

    private fun parsePatterns(input: List<String>): Pair<List<Pair<List<Int>, Int>>, List<Pair<List<Int>, Int>>> {
        val locks = mutableListOf<Pair<List<Int>, Int>>()
        val keys = mutableListOf<Pair<List<Int>, Int>>()

        val currentBlock = mutableListOf<String>()

        for (line in input) {
            if (line.isBlank()) {
                if (currentBlock.isNotEmpty()) {
                    val pattern = calculateColumnFill(currentBlock)
                    if (currentBlock[0][0] == '#') {
                        locks.add(pattern to currentBlock.size)
                    } else {
                        keys.add(pattern to currentBlock.size)
                    }
                    currentBlock.clear()
                }
            } else {
                currentBlock.add(line)
            }
        }

        if (currentBlock.isNotEmpty()) {
            val pattern = calculateColumnFill(currentBlock)
            if (currentBlock[0][0] == '#') {
                locks.add(pattern to currentBlock.size)
            } else {
                keys.add(pattern to currentBlock.size)
            }
        }
        return locks to keys
    }
}

