package cc.woverflow.onecore.websocket.packets

import org.java_websocket.framing.CloseFrame


enum class CloseType constructor(val code: Int) {

    UNKNOWN(0),
    REVOLUTION(9), //xander
    GOODNIGHT(30), //xander x2
    NORMAL(1000),
    GOING_AWAY(1001),
    PROTOCOL_ERROR(1002),
    UNSUPPORTED(1003),
    NO_STATUS(1005),
    ABNORMAL(1006),
    UNSUPPORTED_PAYLOAD(1007),
    POLICY_VIOLATION(1008),
    TOO_LARGE(1009),
    MANDATORY_EXTENSION(1010),
    SERVER_ERROR(1011),
    SERVICE_RESTART(1012),
    TRY_AGAIN_LATER(1013),
    BAD_GATEWAY(1014),
    TLS_HANDSHAKE_FAIL(1015);

    fun asCloseFrame(): CloseFrame = asCloseFrame(this)

    companion object {
        fun valueOf(code: Int): CloseType {
            for (value in values()) {
                if (value.code == code) {
                    return value
                }
            }
            return UNKNOWN
        }

        fun asCloseFrame(type: CloseType): CloseFrame = CloseFrame().also { it.setCode(type.code) }
    }
}