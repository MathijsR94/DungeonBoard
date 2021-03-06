package util

import java.io.File
import javax.swing.ImageIcon
import javax.swing.JButton

/**
 * An alternate to `File.listFiles()` which returns in alphabetical order
 * @return a `LinkedList<File>` of all files in `folder` in alphabetical order
 */
fun File.listFilesInOrder(): List<File> {
    return listFiles()?.sortedWith(compareBy { it.absolutePath }) ?: listOf()
}

/**
 * creates and returns a `JButton` with the proper look
 * @param label text to be shown on the `JButton`
 * @return a `JButton` that looks like the standard for Dungeon Board
 */
fun createButton(label: String): JButton {
    return JButton(label).apply {
        isFocusPainted = false
        isRolloverEnabled = false
    }
}

/**
 * creates and returns a `JButton` with the proper look
 * @param imageIcon to be shown on the `JButton`
 * @return a `JButton` that looks like the standard for Dungeon Board
 */
fun createButton(imageIcon: ImageIcon): JButton {
    return JButton(imageIcon).apply {
        isFocusPainted = false
        isRolloverEnabled = false
    }
}
