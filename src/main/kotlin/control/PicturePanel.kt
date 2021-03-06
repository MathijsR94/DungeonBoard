package control

import util.Colors
import util.Labels
import util.Log
import java.awt.Dimension
import java.awt.GridLayout
import java.awt.Insets
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import javax.swing.*

/**
 * a scroll menu to display images on as buttons
 * @param thumbnailFolder directory to hold thumbnail caches
 * @author McAJBen@gmail.com
 * @since 1.0
 */
abstract class PicturePanel(
    private val thumbnailFolder: File
) : JPanel() {

    companion object {

        private const val serialVersionUID = 2972394170217781329L

        /**
         * The number of picture per row of a picture panel
         */
        private const val GRID_WIDTH = 4

        /**
         * The size of the `ImageIcon` in each of the buttons
         */
        private val IMAGE_ICON_SIZE = Dimension(100, 60)
    }

    init {
        layout = GridLayout(0, GRID_WIDTH)
        border = BorderFactory.createEmptyBorder()
    }

    /**
     * creates a button for a `PicturePanel` by loading an image from file
     * @param file the file of an image to add
     * @return a button with the proper settings for a `PicturePanel`
     */
    fun createPPButton(file: File): JButton {
        createThumbnail(file)

        return JButton(file.name).apply {
            margin = Insets(0, 0, 0, 0)
            isFocusPainted = false
            verticalTextPosition = SwingConstants.TOP
            horizontalTextPosition = SwingConstants.CENTER
            background = Colors.DISABLE_COLOR
            addActionListener {
                val button = it.source as JButton
                val name = button.text
                if (button.background === Colors.DISABLE_COLOR) {
                    select(name)
                    button.background = Colors.ENABLE_COLOR
                } else {
                    deselect(name)
                    button.background = Colors.DISABLE_COLOR
                }
            }
        }
    }

    /**
     * re-sizes an image and saves a lower quality version as a thumbnail
     * @param file the file input of the full size image
     */
    private fun createThumbnail(file: File) {
        val tFile = File(thumbnailFolder, file.name)
        if (!tFile.exists() || file.lastModified() > tFile.lastModified()) {
            try {
                val bufferedImage = BufferedImage(
                    IMAGE_ICON_SIZE.width,
                    IMAGE_ICON_SIZE.height,
                    BufferedImage.TYPE_INT_RGB
                )
                bufferedImage.graphics.drawImage(
                    ImageIO.read(file).getScaledInstance(
                        IMAGE_ICON_SIZE.width,
                        IMAGE_ICON_SIZE.height,
                        BufferedImage.SCALE_SMOOTH
                    ),
                    0, 0, null
                )
                ImageIO.write(bufferedImage, "GIF", tFile)
            } catch (e: Exception) {
                Log.error(Labels.CANNOT_CREATE_THUMBNAIL, e)
            }
        }
    }

    /**
     * removes all images
     */
    fun clearButtons() {
        components.filterIsInstance<JButton>().forEach {
            remove(it)
        }
    }

    /**
     * called when an image is selected
     * @param name the name of the image
     */
    protected abstract fun select(name: String)

    /**
     * called when an image is deselected
     * @param name the name of the image
     */
    protected abstract fun deselect(name: String)

    /**
     * loads the thumbnails from file
     */
    fun rememberThumbnails() {
        components.filterIsInstance<JButton>().forEach {
            try {
                it.icon = ImageIcon(ImageIO.read(File(thumbnailFolder, it.text)))
            } catch (e: Exception) {
                Log.error(Labels.CANNOT_LOAD_THUMBNAIL, e)
            }
        }
    }

    /**
     * removes the thumbnails from local memory
     */
    fun forgetThumbnails() {
        components.filterIsInstance<JButton>().forEach {
            it.icon = null
        }
    }
}