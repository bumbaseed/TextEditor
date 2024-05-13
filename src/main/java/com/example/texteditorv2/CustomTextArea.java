package com.example.texteditorv2;

import org.fxmisc.richtext.StyledTextArea;
import org.fxmisc.richtext.model.StyledDocument;

public class CustomTextArea extends StyledTextArea<String, String> {
    private final PieceTable pieceTable;

    public CustomTextArea() {
        super("",  // default paragraph style
                (paragraph, style) -> {},  // style applicator for paragraphs (no style applied)
                "",  // default segment style
                (text, style) -> {});  // style applicator for segments (no style applied)

        pieceTable = new PieceTable();
        setWrapText(true);
        insertText(0, pieceTable.getText()); // Initialize the editor with text from your piece table
    }

    @Override
    public void replace(int start, int end, StyledDocument<String, String, String> replacement){
        super.replace(start, end, replacement);
        String newText = replacement.getText();
        pieceTable.insert(start, newText);

    }
}