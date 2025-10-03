package ru.frozenpriest.utils

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory
import kotlin.io.path.notExists
import org.w3c.dom.Element


fun Path.updateScanlator(sourceName: String, scanlator: String?) {
    FileSystems.newFileSystem(this, mapOf("create" to "true")).use { zipFileSystem ->
        val comicInfoPath = zipFileSystem.getPath("ComicInfo.xml")
        if (comicInfoPath.notExists()) return

        val xmlBytes = Files.readAllBytes(comicInfoPath)
        val translatorValue = if (scanlator != null) "$sourceName $scanlator" else sourceName
        val updatedXmlBytes = updateTranslatorWithXPath(xmlBytes, translatorValue)

        Files.write(comicInfoPath, updatedXmlBytes)
    }
}

fun updateTranslatorWithXPath(xmlBytes: ByteArray, newTranslatorValue: String): ByteArray {
    val document = DocumentBuilderFactory.newInstance().apply {
        isNamespaceAware = false
    }.newDocumentBuilder().parse(ByteArrayInputStream(xmlBytes))
    document.documentElement.normalize()

    val rootElement = document.documentElement
    if (rootElement.tagName != TAG_COMIC_INFO) {
        throw IllegalArgumentException("Root element is not '$TAG_COMIC_INFO'")
    }

    val translatorNode = XPathFactory.newInstance().newXPath()
        .compile("/$TAG_COMIC_INFO/$TAG_TRANSLATOR")
        .evaluate(document, XPathConstants.NODE) as Element?

    if (translatorNode != null) {
        translatorNode.textContent = newTranslatorValue
    } else {
        val newTranslator = document.createElement(TAG_TRANSLATOR).apply {
            textContent = newTranslatorValue
        }
        rootElement.appendChild(newTranslator)
    }

    val outputStream = ByteArrayOutputStream()
    TransformerFactory.newInstance().newTransformer().apply {
        setOutputProperty(OutputKeys.INDENT, "yes")
        transform(DOMSource(document), StreamResult(outputStream))
    }

    return outputStream.toByteArray()
}

private const val TAG_TRANSLATOR: String = "Translator"
private const val TAG_COMIC_INFO: String = "ComicInfo"