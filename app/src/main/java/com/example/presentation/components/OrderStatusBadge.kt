package com.example.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.OrderStatus
import com.example.ui.theme.PoetraFoodTheme
import com.example.ui.theme.PoetraStatusPaid
import com.example.ui.theme.PoetraStatusPaidBg
import com.example.ui.theme.PoetraStatusPending
import com.example.ui.theme.PoetraStatusPendingBg
import com.example.ui.theme.PoetraStatusPreparing
import com.example.ui.theme.PoetraStatusPreparingBg
import com.example.ui.theme.PoetraStatusReady
import com.example.ui.theme.PoetraStatusReadyBg

@Composable
fun OrderStatusBadge(
    status: OrderStatus,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, icon) = when (status) {
        OrderStatus.PENDING_PAYMENT -> Triple(
            PoetraStatusPendingBg,
            PoetraStatusPending,
            Icons.Default.HourglassTop
        )
        OrderStatus.PAID -> Triple(
            PoetraStatusPaidBg,
            PoetraStatusPaid,
            Icons.Default.Payment
        )
        OrderStatus.PREPARING -> Triple(
            PoetraStatusPreparingBg,
            PoetraStatusPreparing,
            Icons.Default.Restaurant
        )
        OrderStatus.READY -> Triple(
            PoetraStatusReadyBg,
            PoetraStatusReady,
            Icons.Default.CheckCircle
        )
        OrderStatus.COMPLETED -> Triple(
            PoetraStatusReadyBg,
            PoetraStatusReady,
            Icons.Default.DoneAll
        )
        OrderStatus.CANCELLED -> Triple(
            PoetraStatusPendingBg,
            PoetraStatusPending,
            Icons.Default.AccessTime
        )
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bgColor)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = textColor,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = status.label,
            color = textColor,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
@Preview(showBackground = true)
@Composable
fun OrderStatusBadgePreview() {
    PoetraFoodTheme {
        OrderStatusBadge(
            status = OrderStatus.PREPARING
        )
    }
}
