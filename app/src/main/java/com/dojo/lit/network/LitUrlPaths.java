package com.dojo.lit.network;

import androidx.annotation.StringDef;

import org.jetbrains.annotations.NotNull;

public interface LitUrlPaths {
    String CREATE_ROOM = "/createroom";
    String JOIN_ROOM = "/joinroom";
    String LEAVE_ROOM = "/leaveroom";
    String ASK_CARD = "/transaction";
    String DROP_SET = "/drop";
    String TRANSFER_TURN = "/transfer";
    String REMATCH = "/rematch";
    String CHECK_UPGRADE = "/checkappversion";
}
