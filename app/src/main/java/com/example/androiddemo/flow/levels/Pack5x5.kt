package com.example.androiddemo.flow.levels

import com.example.androiddemo.flow.model.Level

// Each level has exactly one way to connect every pair, and that way fills the whole board.
val PACK_5X5 = listOf(
    Level(1, listOf("R...G", ".B..Y", ".O.G.", "..RY.", "B...O")),
    Level(2, listOf("R...B", "G....", ".YGR.", ".O.O.", "...YB")),
    Level(3, listOf("...BG", ".....", "B.R..", "Y...R", "..Y.G")),
    Level(4, listOf("B....", "GR...", "..Y.B", ".YORP", ".G.OP")),
    Level(5, listOf("...G.", ".B.R.", "Y.Y..", "..O..", "BO.RG")),
    Level(6, listOf(".BY.B", ".Y..G", ".....", "...R.", "RG...")),
    Level(7, listOf("....B", "YOY.G", "R.B..", ".O.R.", "...G.")),
    Level(8, listOf("G..GR", "BO.R.", ".Y..O", "....Y", "....B")),
    Level(9, listOf("R..RG", "...O.", "O....", "YBY.G", "....B")),
    Level(10, listOf("....R", "BO..G", "Y....", "...RG", "YO..B")),
    Level(11, listOf("Y....", "O.GBG", "....R", "OC.B.", "C..YR")),
    Level(12, listOf("Y.YB.", "O..GB", ".CO..", ".R.R.", "..CG.")),
    Level(13, listOf("O..OY", "C.BY.", ".B...", ".G.RG", "..C.R")),
    Level(14, listOf(".OR.R", ".C..G", ".OY..", "BY.CG", "....B")),
    Level(15, listOf("OY.B.", ".O.Y.", ".CG.B", "CPR.G", "P...R")),
)
