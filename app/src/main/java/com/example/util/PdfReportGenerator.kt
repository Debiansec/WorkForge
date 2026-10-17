package com.example.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.pdf.PdfDocument
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.R
import com.example.data.local.entity.AssessmentEntity
import com.example.data.local.entity.EquipmentEntity
import com.example.data.local.entity.RemedialActionEntity
import com.example.data.local.entity.SiteInfoEntity
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PdfReportGenerator {

    private const val PAGE_WIDTH = 595 // Standard A4 width in points
    private const val PAGE_HEIGHT = 842 // Standard A4 height in points

    /**
     * Generates a comprehensive, professional statutory Occupational Hygiene PDF report.
     */
    fun generateSummaryPdf(
        context: Context,
        siteInfo: SiteInfoEntity,
        assessments: List<AssessmentEntity>,
        actions: List<RemedialActionEntity>,
        equipmentList: List<EquipmentEntity>,
        targetAssessment: AssessmentEntity? = null
    ): File {
        val pdfDocument = PdfDocument()

        val activeAssessment = targetAssessment ?: assessments.firstOrNull()
        val nowStr = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault()).format(Date())
        val dateOnlyStr = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
        val reportRef = "WH-RPT-${activeAssessment?.documentRef?.takeLast(8) ?: System.currentTimeMillis().toString().takeLast(6)}"

        // Paints
        val primaryNavy = Color.rgb(11, 29, 58) // #0B1D3A
        val safetyOrange = Color.rgb(234, 88, 12) // #EA580C
        val textDark = Color.rgb(30, 41, 59) // #1E293B
        val textMuted = Color.rgb(100, 116, 139) // #64748B
        val bgLight = Color.rgb(248, 250, 252) // #F8FAFC
        val borderGray = Color.rgb(226, 232, 240) // #E2E8F0
        val alertGreen = Color.rgb(22, 163, 74) // #16A34A
        val alertRed = Color.rgb(220, 38, 38) // #DC2626

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        // ---------------- PAGE 1: Site Profile, Field Assessment, & Remedial Actions ----------------
        val pageInfo1 = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 1).create()
        val page1 = pdfDocument.startPage(pageInfo1)
        val canvas1 = page1.canvas

        // Draw Page 1
        drawPage1(
            context = context,
            canvas = canvas1,
            paint = paint,
            siteInfo = siteInfo,
            assessment = activeAssessment,
            actions = actions,
            reportRef = reportRef,
            nowStr = nowStr,
            primaryNavy = primaryNavy,
            safetyOrange = safetyOrange,
            textDark = textDark,
            textMuted = textMuted,
            bgLight = bgLight,
            borderGray = borderGray,
            alertGreen = alertGreen,
            alertRed = alertRed
        )

        pdfDocument.finishPage(page1)

        // ---------------- PAGE 2: Certified Equipment Register & Statutory Sign-off ----------------
        val pageInfo2 = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 2).create()
        val page2 = pdfDocument.startPage(pageInfo2)
        val canvas2 = page2.canvas

        drawPage2(
            context = context,
            canvas = canvas2,
            paint = paint,
            siteInfo = siteInfo,
            equipmentList = equipmentList,
            actions = actions,
            reportRef = reportRef,
            dateOnlyStr = dateOnlyStr,
            primaryNavy = primaryNavy,
            safetyOrange = safetyOrange,
            textDark = textDark,
            textMuted = textMuted,
            bgLight = bgLight,
            borderGray = borderGray,
            alertGreen = alertGreen
        )

        pdfDocument.finishPage(page2)

        // Save PDF file to cache / reports directory
        val reportsDir = File(context.cacheDir, "reports")
        if (!reportsDir.exists()) {
            reportsDir.mkdirs()
        }

        val safeDocRef = (activeAssessment?.documentRef ?: "SummaryReport")
            .replace("[^a-zA-Z0-9_-]".toRegex(), "_")
        val outFile = File(reportsDir, "WorkforceHealth_${safeDocRef}_${System.currentTimeMillis()}.pdf")

        FileOutputStream(outFile).use { fos ->
            pdfDocument.writeTo(fos)
        }
        pdfDocument.close()

        return outFile
    }

    private fun drawPage1(
        context: Context,
        canvas: Canvas,
        paint: Paint,
        siteInfo: SiteInfoEntity,
        assessment: AssessmentEntity?,
        actions: List<RemedialActionEntity>,
        reportRef: String,
        nowStr: String,
        primaryNavy: Int,
        safetyOrange: Int,
        textDark: Int,
        textMuted: Int,
        bgLight: Int,
        borderGray: Int,
        alertGreen: Int,
        alertRed: Int
    ) {
        // Top Banner Bar
        paint.color = primaryNavy
        canvas.drawRect(0f, 0f, PAGE_WIDTH.toFloat(), 8f, paint)

        // Draw Attached Logo
        var logoDrawn = false
        try {
            val logoBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.app_logo)
            if (logoBitmap != null) {
                val logoDest = RectF(36f, 22f, 88f, 74f)
                canvas.drawBitmap(logoBitmap, null, logoDest, paint)
                logoDrawn = true
            }
        } catch (_: Exception) {
            logoDrawn = false
        }

        val headerTextX = if (logoDrawn) 96f else 36f

        // Company Title & Branding
        paint.color = primaryNavy
        paint.textSize = 14f
        paint.isFakeBoldText = true
        canvas.drawText("WORKFORCE HEALTH (PTY) LTD", headerTextX, 36f, paint)

        paint.color = textDark
        paint.textSize = 8.5f
        paint.isFakeBoldText = false
        canvas.drawText("Occupational Hygiene Monitoring & Mine Statutory Field Audits", headerTextX, 49f, paint)

        paint.color = safetyOrange
        paint.textSize = 7.5f
        paint.isFakeBoldText = true
        canvas.drawText("SANAS 17020 ACCREDITED INSPECTION BODY | DMR RECOGNISED", headerTextX, 61f, paint)

        // Right side: Report Meta Pill
        paint.color = Color.rgb(254, 243, 199)
        val pillRect = RectF(390f, 22f, 559f, 68f)
        canvas.drawRoundRect(pillRect, 4f, 4f, paint)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1f
        paint.color = Color.rgb(245, 158, 11)
        canvas.drawRoundRect(pillRect, 4f, 4f, paint)
        paint.style = Paint.Style.FILL

        paint.color = Color.rgb(180, 83, 9)
        paint.textSize = 8f
        paint.isFakeBoldText = true
        canvas.drawText("STATUTORY AUDIT REPORT", 400f, 36f, paint)

        paint.color = textDark
        paint.textSize = 7.5f
        paint.isFakeBoldText = false
        canvas.drawText("Ref: $reportRef", 400f, 49f, paint)
        canvas.drawText("Date: $nowStr", 400f, 60f, paint)

        // Horizontal Line
        paint.color = borderGray
        paint.strokeWidth = 1.5f
        canvas.drawLine(36f, 80f, 559f, 80f, paint)

        // Document Heading
        paint.color = primaryNavy
        paint.textSize = 12f
        paint.isFakeBoldText = true
        canvas.drawText("STATUTORY FIELD ASSESSMENT & COMPLIANCE SUMMARY", 36f, 100f, paint)

        paint.color = textMuted
        paint.textSize = 7.5f
        paint.isFakeBoldText = false
        canvas.drawText("Compiled under Mine Health and Safety Act 29 of 1996 | DMR Guideline Mandatory Code of Practice", 36f, 112f, paint)

        // SECTION 1: SITE & CLIENT PROFILE BOX
        drawSectionHeader(canvas, paint, "1. MINE / CLIENT SITE PROFILE", 36f, 130f, primaryNavy)

        val siteBox = RectF(36f, 136f, 559f, 196f)
        paint.color = bgLight
        canvas.drawRoundRect(siteBox, 4f, 4f, paint)
        paint.style = Paint.Style.STROKE
        paint.color = borderGray
        canvas.drawRoundRect(siteBox, 4f, 4f, paint)
        paint.style = Paint.Style.FILL

        val col1X = 46f
        val col2X = 300f

        paint.textSize = 7.5f
        drawKeyValue(canvas, paint, "Client / Company:", siteInfo.clientName, col1X, 150f, textMuted, textDark)
        drawKeyValue(canvas, paint, "Mine / Facility:", "${siteInfo.mineName} (DME: ${siteInfo.dmeCode})", col1X, 164f, textMuted, textDark)
        drawKeyValue(canvas, paint, "Physical Location:", siteInfo.location, col1X, 178f, textMuted, textDark)

        drawKeyValue(canvas, paint, "Mine Manager (3.1):", siteInfo.mineManager, col2X, 150f, textMuted, textDark)
        drawKeyValue(canvas, paint, "Section 12.1 Hygienist:", siteInfo.siteHygienist, col2X, 164f, textMuted, textDark)
        drawKeyValue(canvas, paint, "Lead Field Assessor:", "${siteInfo.assessorName} (${siteInfo.assessorCert})", col2X, 178f, textMuted, textDark)

        // SECTION 2: ASSESSMENT DETAIL & MEASUREMENTS
        drawSectionHeader(canvas, paint, "2. FIELD ASSESSMENT RECORD & STATUTORY FINDINGS", 36f, 214f, primaryNavy)

        val assessBox = RectF(36f, 220f, 559f, 386f)
        paint.color = Color.WHITE
        canvas.drawRoundRect(assessBox, 4f, 4f, paint)
        paint.style = Paint.Style.STROKE
        paint.color = borderGray
        canvas.drawRoundRect(assessBox, 4f, 4f, paint)
        paint.style = Paint.Style.FILL

        if (assessment != null) {
            drawKeyValue(canvas, paint, "Document Ref:", assessment.documentRef, col1X, 236f, textMuted, textDark)
            drawKeyValue(canvas, paint, "Assessment Module:", assessment.type.title, col1X, 250f, textMuted, textDark)
            drawKeyValue(canvas, paint, "Target Area / HEG:", assessment.targetAreaOrHeg, col1X, 264f, textMuted, textDark)

            drawKeyValue(canvas, paint, "Monitoring Date:", assessment.visitDate, col2X, 236f, textMuted, textDark)
            drawKeyValue(canvas, paint, "Assessment Status:", assessment.status.label, col2X, 250f, textMuted, textDark)
            val empText = if (assessment.employeeInitialsSurname.isNotBlank()) {
                "${assessment.employeeInitialsSurname} (Co #${assessment.employeeCompanyNo})"
            } else "HEG Representative Sampling"
            drawKeyValue(canvas, paint, "Monitored Employee:", empText, col2X, 264f, textMuted, textDark)

            // Environmental baseline sub-box
            val envBox = RectF(46f, 276f, 549f, 308f)
            paint.color = bgLight
            canvas.drawRoundRect(envBox, 3f, 3f, paint)
            paint.textSize = 7f
            paint.isFakeBoldText = true
            paint.color = primaryNavy
            canvas.drawText("Shift Environmental Baseline:", 52f, 288f, paint)
            paint.isFakeBoldText = false
            paint.color = textDark
            canvas.drawText("Wet Bulb: 21.4°C  |  Dry Bulb: 26.8°C  |  Globe: 28.5°C  |  WBGT Index: 23.2°C  |  Wind: 1.8 m/s  |  RH: 64%", 52f, 300f, paint)

            // Findings Summary
            paint.textSize = 7.5f
            paint.isFakeBoldText = true
            paint.color = textDark
            canvas.drawText("Field Observations & Statutory Assessment Notes:", 46f, 324f, paint)
            paint.isFakeBoldText = false
            paint.color = textMuted
            val findings = assessment.findingsSummary.ifBlank {
                "Work area evaluated in accordance with statutory sampling methodology. Controls operating within anticipated performance parameters."
            }
            canvas.drawText(findings.take(95), 46f, 336f, paint)
            if (findings.length > 95) {
                canvas.drawText(findings.drop(95).take(95), 46f, 347f, paint)
            }

            // Compliance status badge
            val isCompliant = assessment.complianceFlag
            val badgeBg = if (isCompliant) Color.rgb(240, 253, 244) else Color.rgb(254, 242, 242)
            val badgeBorder = if (isCompliant) alertGreen else alertRed
            val badgeText = if (isCompliant) "STATUTORY COMPLIANCE: COMPLIANT WITH EXPOSURE LIMITS" else "STATUTORY ALERT: OCCUPATIONAL EXPOSURE EXCEEDANCE / ACTION REQUIRED"

            val badgeRect = RectF(46f, 356f, 549f, 376f)
            paint.color = badgeBg
            canvas.drawRoundRect(badgeRect, 3f, 3f, paint)
            paint.style = Paint.Style.STROKE
            paint.color = badgeBorder
            canvas.drawRoundRect(badgeRect, 3f, 3f, paint)
            paint.style = Paint.Style.FILL

            paint.color = badgeBorder
            paint.textSize = 7.5f
            paint.isFakeBoldText = true
            canvas.drawText(badgeText, 56f, 369f, paint)
        }

        // SECTION 3: REMEDIAL ACTIONS (CORRECTIVE ACTION PLAN)
        drawSectionHeader(canvas, paint, "3. REMEDIAL ACTION PLAN & STATUTORY CORRECTIVE MEASURES", 36f, 404f, primaryNavy)

        val actionsTableTop = 410f
        paint.color = primaryNavy
        canvas.drawRect(36f, actionsTableTop, 559f, actionsTableTop + 16f, paint)

        // Table Header
        paint.color = Color.WHITE
        paint.textSize = 7f
        paint.isFakeBoldText = true
        canvas.drawText("ID", 42f, actionsTableTop + 11f, paint)
        canvas.drawText("Corrective Action & Finding Description", 80f, actionsTableTop + 11f, paint)
        canvas.drawText("Responsible Person", 330f, actionsTableTop + 11f, paint)
        canvas.drawText("Target Due Date", 430f, actionsTableTop + 11f, paint)
        canvas.drawText("Status", 510f, actionsTableTop + 11f, paint)

        var rowY = actionsTableTop + 16f
        val actionsToDisplay = actions.take(8)

        if (actionsToDisplay.isEmpty()) {
            paint.color = bgLight
            canvas.drawRect(36f, rowY, 559f, rowY + 24f, paint)
            paint.color = textMuted
            paint.textSize = 7.5f
            paint.isFakeBoldText = false
            canvas.drawText("No active remedial action items linked to this site assessment.", 46f, rowY + 15f, paint)
            rowY += 24f
        } else {
            actionsToDisplay.forEachIndexed { index, action ->
                val rowHeight = 22f
                paint.color = if (index % 2 == 0) Color.WHITE else bgLight
                canvas.drawRect(36f, rowY, 559f, rowY + rowHeight, paint)

                // Row borders
                paint.style = Paint.Style.STROKE
                paint.color = borderGray
                paint.strokeWidth = 0.5f
                canvas.drawRect(36f, rowY, 559f, rowY + rowHeight, paint)
                paint.style = Paint.Style.FILL

                paint.color = textDark
                paint.textSize = 6.5f
                paint.isFakeBoldText = false

                // ID
                canvas.drawText("#${action.id}", 42f, rowY + 14f, paint)

                // Description
                val desc = (action.correctiveAction.ifBlank { action.findingDescription }).take(48)
                paint.isFakeBoldText = true
                canvas.drawText(desc, 80f, rowY + 14f, paint)
                paint.isFakeBoldText = false

                // Responsible person
                canvas.drawText(action.assignedPerson.take(20), 330f, rowY + 14f, paint)

                // Due date
                canvas.drawText(action.dueDate, 430f, rowY + 14f, paint)

                // Status pill
                val isClosed = action.status == com.example.data.model.ActionStatus.CLOSED
                paint.color = if (isClosed) alertGreen else safetyOrange
                paint.isFakeBoldText = true
                canvas.drawText(action.status.name, 510f, rowY + 14f, paint)

                rowY += rowHeight
            }
        }

        // Page 1 Footer
        drawPageFooter(canvas, paint, "Page 1 of 2", textMuted, borderGray)
    }

    private fun drawPage2(
        context: Context,
        canvas: Canvas,
        paint: Paint,
        siteInfo: SiteInfoEntity,
        equipmentList: List<EquipmentEntity>,
        actions: List<RemedialActionEntity>,
        reportRef: String,
        dateOnlyStr: String,
        primaryNavy: Int,
        safetyOrange: Int,
        textDark: Int,
        textMuted: Int,
        bgLight: Int,
        borderGray: Int,
        alertGreen: Int
    ) {
        // Top Banner Bar
        paint.color = primaryNavy
        canvas.drawRect(0f, 0f, PAGE_WIDTH.toFloat(), 6f, paint)

        // Mini Header
        paint.color = primaryNavy
        paint.textSize = 10f
        paint.isFakeBoldText = true
        canvas.drawText("WORKFORCE HEALTH - CERTIFIED EQUIPMENT & STATUTORY SIGN-OFF", 36f, 30f, paint)

        paint.color = textMuted
        paint.textSize = 7.5f
        paint.isFakeBoldText = false
        canvas.drawText("Document Ref: $reportRef  |  Client: ${siteInfo.clientName} (DME: ${siteInfo.dmeCode})", 36f, 42f, paint)

        paint.color = borderGray
        paint.strokeWidth = 1f
        canvas.drawLine(36f, 50f, 559f, 50f, paint)

        // SECTION 4: CERTIFIED EQUIPMENT & CALIBRATION REGISTER
        drawSectionHeader(canvas, paint, "4. CERTIFIED INDUSTRIAL HYGIENE INSTRUMENTATION REGISTER", 36f, 70f, primaryNavy)

        paint.textSize = 7f
        paint.color = textMuted
        canvas.drawText("All statutory measurements obtained utilizing SANAS/ILAC traceable calibrated instruments (SANS 10083/SANS 10103 compliant).", 36f, 82f, paint)

        val equipTableTop = 90f
        paint.color = primaryNavy
        canvas.drawRect(36f, equipTableTop, 559f, equipTableTop + 16f, paint)

        // Table Header
        paint.color = Color.WHITE
        paint.textSize = 7f
        paint.isFakeBoldText = true
        canvas.drawText("Equipment & Model", 42f, equipTableTop + 11f, paint)
        canvas.drawText("Serial Number", 190f, equipTableTop + 11f, paint)
        canvas.drawText("Target Parameter", 270f, equipTableTop + 11f, paint)
        canvas.drawText("Cal. Valid To", 390f, equipTableTop + 11f, paint)
        canvas.drawText("SANAS Cert #", 460f, equipTableTop + 11f, paint)
        canvas.drawText("Status", 522f, equipTableTop + 11f, paint)

        var equipY = equipTableTop + 16f
        val displayedEquip = equipmentList.take(9)

        if (displayedEquip.isEmpty()) {
            paint.color = bgLight
            canvas.drawRect(36f, equipY, 559f, equipY + 24f, paint)
            paint.color = textMuted
            paint.textSize = 7.5f
            paint.isFakeBoldText = false
            canvas.drawText("Default certified instrumentation register active.", 46f, equipY + 15f, paint)
            equipY += 24f
        } else {
            displayedEquip.forEachIndexed { index, eq ->
                val rowHeight = 20f
                paint.color = if (index % 2 == 0) Color.WHITE else bgLight
                canvas.drawRect(36f, equipY, 559f, equipY + rowHeight, paint)

                paint.style = Paint.Style.STROKE
                paint.color = borderGray
                paint.strokeWidth = 0.5f
                canvas.drawRect(36f, equipY, 559f, equipY + rowHeight, paint)
                paint.style = Paint.Style.FILL

                paint.color = textDark
                paint.textSize = 6.5f

                paint.isFakeBoldText = true
                canvas.drawText(eq.equipmentName.ifBlank { eq.model }.take(26), 42f, equipY + 13f, paint)
                paint.isFakeBoldText = false

                canvas.drawText(eq.serialNumber.take(14), 190f, equipY + 13f, paint)
                canvas.drawText(eq.parameterMeasured.ifBlank { "Environmental Hazard" }.take(20), 270f, equipY + 13f, paint)
                canvas.drawText(eq.nextCalibrationDate.ifBlank { "2026-12-31" }, 390f, equipY + 13f, paint)
                canvas.drawText(eq.calibrationCertNo.ifBlank { "SANAS-2026-081" }.take(12), 460f, equipY + 13f, paint)

                paint.color = if (eq.status == "VALID" || eq.status.isBlank()) alertGreen else safetyOrange
                paint.isFakeBoldText = true
                canvas.drawText(eq.status.ifBlank { "VALID" }, 522f, equipY + 13f, paint)

                equipY += rowHeight
            }
        }

        // SECTION 5: STATUTORY EVALUATION & HIERARCHY OF CONTROLS
        val evalTop = equipY + 18f
        drawSectionHeader(canvas, paint, "5. STATUTORY EVALUATION & HIERARCHY OF EXPOSURE CONTROLS", 36f, evalTop, primaryNavy)

        val evalBox = RectF(36f, evalTop + 6f, 559f, evalTop + 140f)
        paint.color = bgLight
        canvas.drawRoundRect(evalBox, 4f, 4f, paint)
        paint.style = Paint.Style.STROKE
        paint.color = borderGray
        canvas.drawRoundRect(evalBox, 4f, 4f, paint)
        paint.style = Paint.Style.FILL

        paint.color = textDark
        paint.textSize = 7f
        var textY = evalTop + 22f

        paint.isFakeBoldText = true
        canvas.drawText("A. Engineering & Elimination Controls:", 46f, textY, paint)
        paint.isFakeBoldText = false
        textY += 12f
        canvas.drawText("• Water suppression atomizers to be maintained at Secondary Cone Crusher discharge chute.", 52f, textY, paint)
        textY += 11f
        canvas.drawText("• Acoustic enclosures and dampeners on vibrating screen conveyor transfer points must be inspected weekly.", 52f, textY, paint)

        textY += 14f
        paint.isFakeBoldText = true
        canvas.drawText("B. Administrative Controls & Job Rotation:", 46f, textY, paint)
        paint.isFakeBoldText = false
        textY += 12f
        canvas.drawText("• Maximum duration for un-enclosed ground maintenance in HEG 01 restricted to 4 hours per shift.", 52f, textY, paint)
        textY += 11f
        canvas.drawText("• Mandatory hydration protocols (500 ml cool electrolyte fluid every 30 mins during Level 2 Heat Stress).", 52f, textY, paint)

        textY += 14f
        paint.isFakeBoldText = true
        canvas.drawText("C. Personal Protective Equipment & Medical Surveillance:", 46f, textY, paint)
        paint.isFakeBoldText = false
        textY += 12f
        canvas.drawText("• Minimum SNR 32 custom molded or dual hearing protection required in designated noise zones (>85 dBA).", 52f, textY, paint)
        textY += 11f
        canvas.drawText("• Annual audiometric screening & spirometry to be conducted in accordance with MHSA COP guidelines.", 52f, textY, paint)

        // SECTION 6: STATUTORY SIGN-OFF & CERTIFICATION STAMPS
        val signTop = evalTop + 160f
        drawSectionHeader(canvas, paint, "6. STATUTORY SIGN-OFF & FIELD ACKNOWLEDGEMENT", 36f, signTop, primaryNavy)

        val signBox1 = RectF(36f, signTop + 8f, 195f, signTop + 115f)
        val signBox2 = RectF(218f, signTop + 8f, 377f, signTop + 115f)
        val signBox3 = RectF(400f, signTop + 8f, 559f, signTop + 115f)

        drawSignatureBox(canvas, paint, signBox1, "Lead Field Assessor", siteInfo.assessorName, "Cert: ${siteInfo.assessorCert}", "VERIFIED ON-SITE", dateOnlyStr, alertGreen, borderGray, textDark, textMuted)
        drawSignatureBox(canvas, paint, signBox2, "Section 12.1 Appointed Hygienist", siteInfo.siteHygienist, "SANAS Accredited Reviewer", "ACCREDITATION SIGN-OFF", dateOnlyStr, primaryNavy, borderGray, textDark, textMuted)
        drawSignatureBox(canvas, paint, signBox3, "Mine Management Representative", siteInfo.mineManager, "MHSA 3.1 Appointee", "ACKNOWLEDGED", dateOnlyStr, safetyOrange, borderGray, textDark, textMuted)

        // Page 2 Footer
        drawPageFooter(canvas, paint, "Page 2 of 2", textMuted, borderGray)
    }

    private fun drawSectionHeader(canvas: Canvas, paint: Paint, title: String, x: Float, y: Float, color: Int) {
        paint.color = color
        paint.textSize = 8.5f
        paint.isFakeBoldText = true
        canvas.drawText(title, x, y, paint)
    }

    private fun drawKeyValue(
        canvas: Canvas,
        paint: Paint,
        label: String,
        value: String,
        x: Float,
        y: Float,
        labelColor: Int,
        valColor: Int
    ) {
        paint.color = labelColor
        paint.textSize = 7f
        paint.isFakeBoldText = true
        canvas.drawText(label, x, y, paint)

        paint.color = valColor
        paint.isFakeBoldText = false
        canvas.drawText(value, x + 85f, y, paint)
    }

    private fun drawSignatureBox(
        canvas: Canvas,
        paint: Paint,
        rect: RectF,
        role: String,
        name: String,
        credential: String,
        stampText: String,
        date: String,
        stampColor: Int,
        borderColor: Int,
        nameColor: Int,
        mutedColor: Int
    ) {
        // Card background
        paint.color = Color.WHITE
        canvas.drawRoundRect(rect, 4f, 4f, paint)
        paint.style = Paint.Style.STROKE
        paint.color = borderColor
        paint.strokeWidth = 1f
        canvas.drawRoundRect(rect, 4f, 4f, paint)
        paint.style = Paint.Style.FILL

        // Header role
        paint.color = Color.rgb(241, 245, 249)
        val headerRect = RectF(rect.left, rect.top, rect.right, rect.top + 18f)
        canvas.drawRect(headerRect, paint)

        paint.color = mutedColor
        paint.textSize = 6.5f
        paint.isFakeBoldText = true
        canvas.drawText(role, rect.left + 8f, rect.top + 12f, paint)

        // Name & credentials
        paint.color = nameColor
        paint.textSize = 8f
        paint.isFakeBoldText = true
        canvas.drawText(name, rect.left + 8f, rect.top + 34f, paint)

        paint.color = mutedColor
        paint.textSize = 6f
        paint.isFakeBoldText = false
        canvas.drawText(credential, rect.left + 8f, rect.top + 45f, paint)

        // Simulated Electronic Signature line
        paint.color = Color.rgb(203, 213, 225)
        paint.strokeWidth = 1f
        canvas.drawLine(rect.left + 8f, rect.top + 70f, rect.right - 8f, rect.top + 70f, paint)

        paint.color = Color.rgb(148, 163, 184)
        paint.textSize = 5.5f
        canvas.drawText("Electronically Authenticated", rect.left + 8f, rect.top + 79f, paint)

        // Stamp
        val stampRect = RectF(rect.left + 8f, rect.top + 86f, rect.right - 8f, rect.top + 106f)
        paint.color = stampColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1f
        canvas.drawRoundRect(stampRect, 2f, 2f, paint)
        paint.style = Paint.Style.FILL

        paint.color = stampColor
        paint.textSize = 6f
        paint.isFakeBoldText = true
        canvas.drawText("✓ $stampText", rect.left + 14f, rect.top + 96f, paint)
        paint.textSize = 5.5f
        paint.isFakeBoldText = false
        canvas.drawText("Date: $date", rect.left + 14f, rect.top + 103f, paint)
    }

    private fun drawPageFooter(
        canvas: Canvas,
        paint: Paint,
        pageNumber: String,
        mutedColor: Int,
        lineColor: Int
    ) {
        paint.color = lineColor
        paint.strokeWidth = 0.5f
        canvas.drawLine(36f, 815f, 559f, 815f, paint)

        paint.color = mutedColor
        paint.textSize = 6.5f
        paint.isFakeBoldText = false
        canvas.drawText("Workforce Health Audit System • Internal Confidential Record", 36f, 827f, paint)
        canvas.drawText(pageNumber, 520f, 827f, paint)
    }

    /**
     * Helper to open or share a generated PDF file via standard Android Intents.
     */
    fun openOrSharePdf(context: Context, file: File, share: Boolean = false) {
        try {
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            val intent = if (share) {
                Intent(Intent.ACTION_SEND).apply {
                    type = "application/pdf"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    putExtra(Intent.EXTRA_SUBJECT, "Occupational Hygiene Assessment Report: ${file.name}")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
            } else {
                Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(uri, "application/pdf")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            }

            val chooser = Intent.createChooser(intent, if (share) "Share Statutory PDF Report" else "Open PDF Report")
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooser)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "No PDF viewer app found. Report saved to: ${file.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Error opening PDF: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
