package top.sanqii.nwunet

import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*

fun getMacAddress(): String? {
    try {
        val all: List<NetworkInterface> = Collections.list(NetworkInterface.getNetworkInterfaces())
        for (nif in all) {
            if (nif.name != "wlan0") {
                continue
            }
            val macBytes: ByteArray = nif.hardwareAddress ?: return ""
            val res1 = StringBuilder()
            for (b in macBytes) {
                res1.append(String.format("%02X:", b))
            }
            if (res1.isNotEmpty()) {
                res1.deleteCharAt(res1.length - 1)
            }
            return res1.toString()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

fun getIpAddress(): String? {
    try {
        val en: Enumeration<NetworkInterface> = NetworkInterface.getNetworkInterfaces()
        while (en.hasMoreElements()) {
            val nwInterface: NetworkInterface = en.nextElement()
            val enumIpAdder: Enumeration<InetAddress> = nwInterface.inetAddresses
            while (enumIpAdder.hasMoreElements()) {
                val netAddress: InetAddress = enumIpAdder.nextElement()
                if (!netAddress.isLoopbackAddress  && !netAddress.isLinkLocalAddress) {
                    return netAddress.hostAddress.toString()
                }
            }
        }
    } catch (e: SocketException) {
        e.printStackTrace()
    }
    return null
}
