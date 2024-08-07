package com.schoolprojects.corrreps.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.schoolprojects.corrreps.R
import com.schoolprojects.corrreps.ui.theme.Typography

@Composable
fun LogoAndBankName() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Display logo image
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Bank Logo",
            modifier = Modifier
                .size(100.dp) // Adjust size as needed
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Display bank name
        Text(
            text = "Correps",
            style = Typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary // Adjust color as needed
        )

        // Display bank name
        Text(
            text = "A simulation app for course registration and result generation for Caritas University",
            style = Typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary // Adjust color as needed
        )
    }
}