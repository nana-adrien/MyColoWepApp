package empire.digiprem.mycoloapp.core.design_system.extension

// ── commonMain ────────────────────────────────────────────────────
// Le renderer PdfKmp est maintenant dans commonMain
// car PdfKmp supporte Android nativement

import com.conamobile.pdfkmp.PdfDocument
import com.conamobile.pdfkmp.pdf
import com.conamobile.pdfkmp.style.PdfColor
import com.conamobile.pdfkmp.style.BorderStroke
import com.conamobile.pdfkmp.layout.Padding
import com.conamobile.pdfkmp.layout.HorizontalArrangement
import com.conamobile.pdfkmp.layout.VerticalAlignment
import com.conamobile.pdfkmp.layout.BoxAlignment
import com.conamobile.pdfkmp.layout.PageBreakStrategy
import com.conamobile.pdfkmp.table.TableColumn
import com.conamobile.pdfkmp.table.TableBorder
import com.conamobile.pdfkmp.page.PageSize
import com.conamobile.pdfkmp.unit.dp
import com.conamobile.pdfkmp.unit.sp
import empire.digiprem.mycoloapp.core.design_system.components.document_generator.pdf_builder_tools.PdfCell
import empire.digiprem.mycoloapp.core.design_system.components.document_generator.pdf_builder_tools.PdfDocumentStructure
import empire.digiprem.mycoloapp.core.design_system.components.document_generator.pdf_builder_tools.PdfNode
import empire.digiprem.mycoloapp.core.design_system.components.document_generator.pdf_builder_tools.PdfPageSize
import empire.digiprem.mycoloapp.core.design_system.components.document_generator.pdf_builder_tools.PdfTableColumn

internal fun PdfDocumentStructure.toPdfKmpDocument(): PdfDocument = pdf {

    metadata {
        title = this@toPdfKmpDocument.metadata.title
        this@toPdfKmpDocument.metadata.author?.let { author = it }
    }

    defaultPagePadding = Padding.symmetric(horizontal = 32.dp, vertical = 32.dp)
    defaultPageBreakStrategy = PageBreakStrategy.MoveToNextPage

    this@toPdfKmpDocument.pages.forEach { pdfPage ->

        val footerNode = pdfPage.nodes.filterIsInstance<PdfNode.Footer>().firstOrNull()

        page(size = pdfPage.size.toPdfKmpSize()) {

            footerNode?.let { f ->
                footer { ctx ->
                    divider(thickness = 1.dp, color = PdfColor.fromHex("#EEEEEE"))
                    spacer(height = 10.dp)
                    row(horizontalArrangement = HorizontalArrangement.SpaceBetween) {
                        text(f.left ?: "") {
                            fontSize = 10.sp
                            color = PdfColor.fromHex("#9E9E9E")
                        }
                        text(f.center ?: "") {
                            fontSize = 10.sp
                            bold = true
                            color = PdfColor.fromHex("#1565C0")
                        }
                        if (f.showPageNumber) {
                            text("Page ${ctx.pageNumber} / ${ctx.totalPages}") {
                                fontSize = 10.sp
                                color = PdfColor.fromHex("#9E9E9E")
                            }
                        }
                    }
                }
            }

            pdfPage.nodes
                .filter { it !is PdfNode.Footer }
                .forEach { node -> renderNode(node) }
        }
    }
}

// ── renderNode dans commonMain ────────────────────────────────────

private fun renderNode(node: PdfNode) {
    when (node) {

        is PdfNode.Header -> {
            row(
                verticalAlignment = VerticalAlignment.Center,
                spacing = 16.dp,
            ) {
                node.logoText?.let { logo ->
                    box(
                        width = 48.dp,
                        height = 48.dp,
                        background = PdfColor.fromHex(node.logoColor),
                        cornerRadius = 24.dp,
                    ) {
                        aligned(BoxAlignment.Center) {
                            text(logo) {
                                fontSize = 16.sp
                                bold = true
                                color = PdfColor.White
                            }
                        }
                    }
                }
                column(spacing = 3.dp) {
                    text(node.title) {
                        fontSize = 22.sp
                        bold = true
                        color = PdfColor.fromHex(node.logoColor)
                    }
                    node.subtitle?.let {
                        text(it) {
                            fontSize = 12.sp
                            color = PdfColor.fromHex("#9E9E9E")
                        }
                    }
                }
            }
            spacer(height = 16.dp)
            divider(thickness = 3.dp, color = PdfColor.fromHex(node.logoColor))
        }

        is PdfNode.Title -> text(node.text) {
            fontSize = node.size.sp
            bold = node.bold
            node.color?.let { color = PdfColor.fromHex(it) }
        }

        is PdfNode.Paragraph -> text(node.text) {
            fontSize = node.size.sp
            node.color?.let { color = PdfColor.fromHex(it) }
        }

        is PdfNode.Spacer -> spacer(height = node.height.dp)

        is PdfNode.Divider -> divider(
            thickness = node.thickness.dp,
            color = PdfColor.fromHex(node.color),
        )

        is PdfNode.HorizontalRule -> divider(
            thickness = 0.5.dp,
            color = PdfColor.fromHex(node.color),
        )

        is PdfNode.KeyValueList -> {
            if (node.columns == 2) {
                node.items.chunked(2).forEach { pair ->
                    row(spacing = 16.dp) {
                        pair.forEach { (key, value) ->
                            weighted(1f) {
                                column(spacing = 2.dp) {
                                    text(key) {
                                        fontSize = 10.sp
                                        color = PdfColor.fromHex("#9E9E9E")
                                    }
                                    text(value) {
                                        fontSize = 12.sp
                                        bold = true
                                    }
                                }
                            }
                        }
                    }
                    spacer(height = 10.dp)
                }
            } else {
                node.items.forEach { (key, value) ->
                    row(
                        spacing = 8.dp,
                        verticalAlignment = VerticalAlignment.Center,
                    ) {
                        text(key) {
                            fontSize = 11.sp
                            color = PdfColor.fromHex("#9E9E9E")
                        }
                        text(value) {
                            fontSize = 11.sp
                            bold = true
                        }
                    }
                    spacer(height = 6.dp)
                }
            }
        }

        is PdfNode.ProfileCard -> {
            card(
                background = PdfColor.fromHex(node.accentColor).withAlpha(0.05f),
                cornerRadius = 12.dp,
                border = BorderStroke(1.dp, PdfColor.fromHex(node.accentColor).withAlpha(0.2f)),
                padding = Padding.all(16.dp),
            ) {
                row(spacing = 16.dp, verticalAlignment = VerticalAlignment.Center) {
                    box(
                        width = 56.dp,
                        height = 56.dp,
                        background = PdfColor.fromHex(node.accentColor),
                        cornerRadius = 28.dp,
                    ) {
                        aligned(BoxAlignment.Center) {
                            text(node.initials) {
                                fontSize = 20.sp
                                bold = true
                                color = PdfColor.White
                            }
                        }
                    }
                    column(spacing = 4.dp) {
                        text(node.name) { fontSize = 18.sp; bold = true }
                        node.role?.let {
                            text(it) {
                                fontSize = 12.sp
                                color = PdfColor.fromHex("#757575")
                            }
                        }
                    }
                }
                if (node.fields.isNotEmpty()) {
                    spacer(height = 14.dp)
                    divider(thickness = 0.5.dp, color = PdfColor.fromHex("#EEEEEE"))
                    spacer(height = 14.dp)
                    node.fields.chunked(2).forEach { pair ->
                        row(spacing = 16.dp) {
                            pair.forEach { (key, value) ->
                                weighted(1f) {
                                    column(spacing = 2.dp) {
                                        text(key) {
                                            fontSize = 10.sp
                                            color = PdfColor.fromHex("#9E9E9E")
                                        }
                                        text(value) { fontSize = 12.sp }
                                    }
                                }
                            }
                        }
                        spacer(height = 10.dp)
                    }
                }
            }
        }

        is PdfNode.Table -> {
            table(
                columns = node.columns.map { col ->
                    when (col) {
                        is PdfTableColumn.Weight -> TableColumn.Weight(col.weight)
                        is PdfTableColumn.Fixed  -> TableColumn.Fixed(col.widthDp.dp)
                    }
                },
                border = TableBorder(
                    color = PdfColor.fromHex("#EEEEEE"),
                    width = 0.5.dp,
                ),
                cellPadding = Padding.symmetric(horizontal = 10.dp, vertical = 8.dp),
            ) {
                header(background = PdfColor.fromHex(node.accentColor)) {
                    node.columns.forEach { col ->
                        cell(col.header) {
                            bold = true
                            color = PdfColor.White
                            fontSize = 12.sp
                        }
                    }
                }
                node.rows.forEachIndexed { i, row ->
                    val bg = if (node.striped && i % 2 != 0)
                        PdfColor.fromHex("#FAFAFA") else PdfColor.White
                    row(background = bg) {
                        node.columns.forEach { col ->
                            when (val cell = row.cells[col.key] ?: PdfCell.Text("—")) {
                                is PdfCell.Text -> cell(cell.value) {
                                    fontSize = 12.sp
                                    color = if (cell.muted) PdfColor.fromHex("#9E9E9E")
                                    else PdfColor.fromHex("#212121")
                                }
                                is PdfCell.Badge -> cell {
                                    card(
                                        background = PdfColor.fromHex(cell.color).withAlpha(0.12f),
                                        cornerRadius = 4.dp,
                                        padding = Padding.symmetric(horizontal = 6.dp, vertical = 2.dp),
                                    ) {
                                        text(cell.value) {
                                            fontSize = 10.sp
                                            bold = true
                                            color = PdfColor.fromHex(cell.color)
                                        }
                                    }
                                }
                                is PdfCell.MultiLine -> cell {
                                    text(cell.primary) { fontSize = 12.sp; bold = true }
                                    text(cell.secondary) {
                                        fontSize = 10.sp
                                        color = PdfColor.fromHex("#9E9E9E")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        is PdfNode.PageBreak, is PdfNode.Footer -> Unit
    }
}

private fun PdfPageSize.toPdfKmpSize() = when (this) {
    PdfPageSize.A4     -> PageSize.A4
    PdfPageSize.A3     -> PageSize.A3
    PdfPageSize.A5     -> PageSize.A5
    PdfPageSize.Letter -> PageSize.Letter
    PdfPageSize.Legal  -> PageSize.Legal
}