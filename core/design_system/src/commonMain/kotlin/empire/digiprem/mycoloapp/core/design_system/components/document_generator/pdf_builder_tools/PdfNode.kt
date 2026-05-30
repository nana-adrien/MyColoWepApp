package empire.digiprem.mycoloapp.core.design_system.components.document_generator.pdf_builder_tools

sealed class PdfNode {

    // ── Mise en page ─────────────────────────────────────────────
    data class Header(
        val logoText: String? = null,
        val logoColor: String = "#1565C0",
        val title: String,
        val subtitle: String? = null,
    ) : PdfNode()

    data class Spacer(val height: Float = 16f) : PdfNode()

    data class Divider(
        val thickness: Float = 1f,
        val color: String = "#EEEEEE",
    ) : PdfNode()

    data class Footer(
        val left: String? = null,
        val center: String? = null,
        val showPageNumber: Boolean = true,
    ) : PdfNode()

    // ── Contenu ──────────────────────────────────────────────────
    data class Title(
        val text: String,
        val size: Float = 18f,
        val color: String? = null,
        val bold: Boolean = true,
    ) : PdfNode()

    data class Paragraph(
        val text: String,
        val size: Float = 12f,
        val color: String? = null,
    ) : PdfNode()

    data class KeyValueList(
        val items: List<Pair<String, String>>,
        val columns: Int = 1,           // 1 = liste verticale, 2 = deux colonnes
    ) : PdfNode()

    data class Table(
        val columns: List<PdfTableColumn>,
        val rows: List<PdfTableRow>,
        val accentColor: String = "#1565C0",
        val striped: Boolean = true,
    ) : PdfNode()

    data class ProfileCard(
        val initials: String,
        val name: String,
        val role: String? = null,
        val fields: List<Pair<String, String>> = emptyList(),
        val accentColor: String = "#1565C0",
    ) : PdfNode()

    data class HorizontalRule(val color: String = "#EEEEEE") : PdfNode()

    data class PageBreak(val dummy: Unit = Unit) : PdfNode()
}