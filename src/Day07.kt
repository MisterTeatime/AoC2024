fun main() {
    fun part1(input: List<String>): Long {
        return input
            .map { checkExpressionValidity(it)}
            .filter { it.first }
            .sumOf { it.second }
    }

    fun part2(input: List<String>): Long {
        return input
            .map { checkExpressionValidity(it, 2)}
            .filter { it.first }
            .sumOf { it.second }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    val resultPart1 = part1(testInput)
    println("Test Part 1: $resultPart1")
    check(resultPart1 == 3749L)

    val resultPart2 = part2(testInput)
    println("Test Part 2: $resultPart2")
    check(resultPart2 == 11387L)

    val input = readInput("Day07")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

fun checkExpressionValidity(line: String, part: Int = 1): Pair<Boolean, Long> {
    val (targetString, elementsString) = line.split(": ")
    val target = targetString.toLong()
    val elements = elementsString.split(" ").map { it.toLong()}

    return findValidExpression(elements, target, part) to target
}

fun findValidExpression(
    elements: List<Long>,
    target: Long,
    part: Int = 1,
    currentResult: Long = 0,
    currentIndex: Int = 0)
: Boolean {
    if (currentIndex == elements.size) {
        return currentResult == target
    }

    val currentElement = elements[currentIndex]

    if (currentIndex == 0)
        return findValidExpression(
            elements, target, part,
            currentResult = currentElement,
            currentIndex = currentIndex + 1
        )

    //Versuche +
    if (findValidExpression(
        elements, target, part,
        currentResult = currentResult + currentElement,
        currentIndex = currentIndex + 1
    ))
        return true

    //Versuche *
    if (findValidExpression(
        elements, target, part,
        currentResult = currentResult * currentElement,
        currentIndex = currentIndex + 1
    ))
        return true

    //Versuche ||
    if (part == 2)
        if (findValidExpression(
                elements, target, part,
                currentResult = (currentResult.toString() + currentElement.toString()).toLong(),
                currentIndex = currentIndex + 1
            ))
            return true

    return false
}
