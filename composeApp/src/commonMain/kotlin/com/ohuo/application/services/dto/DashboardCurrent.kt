package com.ohuo.application.services.dto

import kotlinx.serialization.Serializable

@Serializable
data class DashboardCurrent(
    val cpuPercent: List<Double>? = null,
    val cpuTotal: Int = 0,
    val cpuUsed: Double = 0.00,
    val cpuUsedPercent: Double = 0.00,
    val diskData: List<DiskData>? = null,
    val gpuData: List<GpuData>? = null,
    val ioCount: Long = 0,
    val ioReadBytes: Long = 0,
    val ioReadTime: Long = 0,
    val ioWriteBytes: Long = 0,
    val ioWriteTime: Long = 0,
    val load1: Double = 0.00,
    val load15: Double = 0.00,
    val load5: Double = 0.00,
    val loadUsagePercent: Double = 0.00,
    val memoryAvailable: Long = 0,
    val memoryTotal: Long = 0,
    val memoryUsed: Long = 0,
    val memoryUsedPercent: Double = 0.00,
    val netBytesRecv: Long = 0,
    val netBytesSent: Long = 0,
    val procs: Long = 0,
    val shotTime: String = "",
    val swapMemoryAvailable: Long = 0,
    val swapMemoryTotal: Long = 0,
    val swapMemoryUsed: Long = 0,
    val swapMemoryUsedPercent: Double = 0.00,
    val timeSinceUptime: String= "",
    val uptime: Long = 0,
    val xpuData: List<XpuData>? = null
)

@Serializable
data class DiskData(
    val device: String,
    val free: Long,
    val inodesFree: Long,
    val inodesTotal: Long,
    val inodesUsed: Long,
    val inodesUsedPercent: Double,
    val path: String,
    val total: Long,
    val type: String,
    val used: Long,
    val usedPercent: Double
)
@Serializable
data class GpuData(
    val fanSpeed: String,
    val gpuUtil: String,
    val index: Int,
    val maxPowerLimit: String,
    val memTotal: String,
    val memUsed: String,
    val memoryUsage: String,
    val performanceState: String,
    val powerDraw: String,
    val powerUsage: String,
    val productName: String,
    val temperature: String
)
@Serializable
data class XpuData(
    val deviceID: Int,
    val deviceName: String,
    val memory: String,
    val memoryUsed: String,
    val memoryUtil: String,
    val power: String,
    val temperature: String
)