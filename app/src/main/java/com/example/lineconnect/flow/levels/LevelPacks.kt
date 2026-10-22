package com.example.lineconnect.flow.levels

import com.example.lineconnect.flow.model.BoardSize
import com.example.lineconnect.flow.model.Level

/** Built-in levels for each board size, numbered from 1 within their pack. */
val LEVEL_PACKS: Map<BoardSize, List<Level>> = mapOf(
    BoardSize.FIVE to PACK_5X5,
    BoardSize.SEVEN to PACK_7X7,
    BoardSize.TEN to PACK_10X10,
)
