import java.io.File

var path = ""
var outPath = ""

fun main(){
    path = "/Users/lukasgrimm/Downloads"
    outPath = "/Users/lukasgrimm/Downloads/Out"
    var randomFile = getRandomFile()
    createClip(randomFile)
}

/**
 * Zufällige Datei aus angegebenen Unter-/Ordner auswählen
 */
fun getRandomFile(): File {
    val randomFile = File(path).walk()
        .filter { file -> !file.isHidden && file.isFile }
        .shuffled()
        .first()

    println("Random File: ${randomFile.path}")

    return randomFile
}

/**
 * Länge des zufälligen Videos auslesen,
 * zufällige Länge bestimmen,
 * Clip schneiden und in Out-Ordner ablegen
 */
fun createClip(randomFile: File) {
    val process = Runtime.getRuntime().exec("ffprobe -v error -select_streams v:0 -show_entries stream=duration -of default=noprint_wrappers=1:nokey=1 ${randomFile.path}")
    var length = process.inputStream.bufferedReader().lineSequence().joinToString("\n")
    length = length.substring(0,length.indexOf("."))
    val endpoint = (10..length.toInt()).random()
    println("Start: ${endpoint-10}, End: $endpoint")
    val outFile = outPath + "/Clip" + randomFile.name
    Runtime.getRuntime().exec("ffmpeg -ss ${endpoint -10} -to $endpoint -i ${randomFile.path} -c copy $outFile")
    println("ffmpeg -ss ${endpoint -10} -to $endpoint -i ${randomFile.path} -c copy $outFile")
}