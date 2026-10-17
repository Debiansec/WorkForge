package com.example.ui.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.Path as AndroidPath
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CorporateNavy800
import com.example.ui.theme.CorporateNavy900
import com.example.ui.theme.SafetyOrangePrimary
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.StatusSuccess
import java.io.File
import java.io.FileOutputStream

@Composable
fun SignaturePad(
    title: String = "Employee Acknowledgement & Signature",
    subtitle: String = "Sign below using touch or stylus to verify field inspection",
    initialSignatureStatus: String = "",
    assessmentRef: String = "LC-AUDIT",
    onSignatureConfirmed: (String) -> Unit
) {
    val context = LocalContext.current
    val lines = remember { mutableStateListOf<List<Offset>>() }
    var currentLine by remember { mutableStateOf<List<Offset>>(emptyList()) }
    var savedSignaturePath by remember { mutableStateOf(initialSignatureStatus) }
    var isConfirmed by remember { mutableStateOf(initialSignatureStatus.isNotBlank()) }

    // Try loading existing saved bitmap file if exists
    val existingBitmap = remember(savedSignaturePath) {
        if (savedSignaturePath.isNotBlank()) {
            val file = File(savedSignaturePath)
            if (file.exists()) {
                BitmapFactory.decodeFile(file.absolutePath)
            } else null
        } else null
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("signature_card"),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Draw,
                        contentDescription = "Signature Icon",
                        tint = SafetyOrangePrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                if (isConfirmed) {
                    Surface(
                        color = StatusSuccess.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Signed",
                                tint = StatusSuccess,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Signed & Sealed",
                                style = MaterialTheme.typography.labelSmall,
                                color = StatusSuccess,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Signature Drawing Canvas Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFFCFDFE))
                    .border(
                        width = if (isConfirmed) 2.dp else 1.dp,
                        color = if (isConfirmed) StatusSuccess else Slate400,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .pointerInput(!isConfirmed) {
                        if (!isConfirmed) {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    currentLine = listOf(offset)
                                },
                                onDragEnd = {
                                    if (currentLine.isNotEmpty()) {
                                        lines.add(currentLine)
                                        currentLine = emptyList()
                                    }
                                }
                            ) { change, _ ->
                                change.consume()
                                currentLine = currentLine + change.position
                            }
                        }
                    }
            ) {
                if (existingBitmap != null && lines.isEmpty()) {
                    Image(
                        bitmap = existingBitmap.asImageBitmap(),
                        contentDescription = "Saved Signature Bitmap",
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        // Draw existing saved lines
                        for (line in lines) {
                            if (line.size > 1) {
                                val path = Path().apply {
                                    moveTo(line.first().x, line.first().y)
                                    for (i in 1 until line.size) {
                                        lineTo(line[i].x, line[i].y)
                                    }
                                }
                                drawPath(
                                    path = path,
                                    color = CorporateNavy900,
                                    style = Stroke(
                                        width = 4f,
                                        cap = StrokeCap.Round,
                                        join = StrokeJoin.Round
                                    )
                                )
                            }
                        }

                        // Draw current dragging line
                        if (currentLine.size > 1) {
                            val path = Path().apply {
                                moveTo(currentLine.first().x, currentLine.first().y)
                                for (i in 1 until currentLine.size) {
                                    lineTo(currentLine[i].x, currentLine[i].y)
                                }
                            }
                            drawPath(
                                path = path,
                                color = CorporateNavy900,
                                style = Stroke(
                                    width = 4f,
                                    cap = StrokeCap.Round,
                                    join = StrokeJoin.Round
                                )
                            )
                        }

                        // Baseline guide line
                        drawLine(
                            color = Slate200,
                            start = Offset(40f, size.height - 35f),
                            end = Offset(size.width - 40f, size.height - 35f),
                            strokeWidth = 1.5f
                        )
                    }
                }

                if (lines.isEmpty() && currentLine.isEmpty() && !isConfirmed && existingBitmap == null) {
                    Text(
                        text = "Touch or stylus to sign on line (X)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                if (isConfirmed && lines.isEmpty() && existingBitmap == null) {
                    Text(
                        text = "✓ Electronic Signature Recorded",
                        style = MaterialTheme.typography.bodyMedium,
                        color = StatusSuccess,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            if (savedSignaturePath.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "File URI: ${savedSignaturePath.substringAfterLast("/")}",
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = {
                        lines.clear()
                        currentLine = emptyList()
                        isConfirmed = false
                        savedSignaturePath = ""
                        onSignatureConfirmed("")
                    },
                    modifier = Modifier.testTag("clear_signature_button")
                ) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Clear")
                }

                Spacer(modifier = Modifier.width(12.dp))

                Button(
                    onClick = {
                        if (lines.isNotEmpty() || !isConfirmed) {
                            val savedPath = saveSignatureAsBitmap(
                                context = context,
                                lines = lines,
                                assessmentRef = assessmentRef
                            )
                            savedSignaturePath = savedPath
                            isConfirmed = true
                            onSignatureConfirmed(savedPath)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isConfirmed) StatusSuccess else SafetyOrangePrimary
                    ),
                    modifier = Modifier.testTag("confirm_signature_button")
                ) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = "Save")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (isConfirmed) "Signature Saved" else "Save & Seal Signature")
                }
            }
        }
    }
}

/**
 * Converts user gesture stroke lines into an on-disk PNG bitmap linked to the assessment record.
 */
private fun saveSignatureAsBitmap(
    context: Context,
    lines: List<List<Offset>>,
    assessmentRef: String
): String {
    val dir = File(context.filesDir, "signatures").apply { mkdirs() }
    val timeStamp = System.currentTimeMillis()
    val sanitizedRef = assessmentRef.replace(Regex("[^a-zA-Z0-9]"), "_")
    val file = File(dir, "SIG_${sanitizedRef}_${timeStamp}.png")

    try {
        val width = 700
        val height = 350
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bitmap)

        // Clean white background
        canvas.drawColor(android.graphics.Color.WHITE)

        val paint = Paint().apply {
            color = android.graphics.Color.rgb(11, 19, 43) // Deep Corporate Navy
            strokeWidth = 7f
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
            isAntiAlias = true
        }

        // Draw each line from the gesture canvas
        for (line in lines) {
            if (line.size > 1) {
                val androidPath = AndroidPath().apply {
                    moveTo(line.first().x, line.first().y)
                    for (i in 1 until line.size) {
                        lineTo(line[i].x, line[i].y)
                    }
                }
                canvas.drawPath(androidPath, paint)
            }
        }

        // Add subtle verification watermark
        val watermarkPaint = Paint().apply {
            color = android.graphics.Color.argb(90, 100, 116, 139)
            textSize = 18f
            isAntiAlias = true
        }
        canvas.drawText("Digitally Signed • Ref: $assessmentRef", 30f, height - 20f, watermarkPaint)

        val out = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        out.flush()
        out.close()
        return file.absolutePath
    } catch (e: Exception) {
        return "SIG_${sanitizedRef}_$timeStamp"
    }
}
