package my.edu.aiu.app.tdminsight.ui.camera

fun extractConcentrationCandidates(text: String): List<Double> {
    val candidates = mutableListOf<Double>()
    
    // Regex matching numbers followed by mg/L or mcg/mL with optional whitespace
    // Numbers can use either . or , as a decimal separator
    val regex = Regex("""([0-9]+(?:[.,][0-9]+)?)\s*(?:mg/l|mcg/ml)""", RegexOption.IGNORE_CASE)
    
    val matches = regex.findAll(text)
    for (match in matches) {
        val numberStr = match.groups[1]?.value ?: continue
        // Standardize the decimal separator to dot for parsing
        val normalizedStr = numberStr.replace(',', '.')
        val value = normalizedStr.toDoubleOrNull()
        if (value != null) {
            candidates.add(value)
        }
    }
    
    return candidates
}
