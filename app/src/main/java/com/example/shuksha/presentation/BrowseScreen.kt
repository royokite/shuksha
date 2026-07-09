package com.example.shuksha.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.shuksha.data.AreaItem
import com.example.shuksha.data.CategoryItem

enum class BrowseSection {
    CATEGORIES,
    CUISINE,
    LETTERS,
    NONE
}

@Composable
fun BrowseScreen(
    categories: List<CategoryItem>,
    areas: List<AreaItem>,
    isLoadingCategories: Boolean,
    isLoadingAreas: Boolean,
    errorMessage: String?,
    onCategoryClick: (String) -> Unit,
    onAreaClick: (String) -> Unit,
    onLetterClick: (String) -> Unit
) {
    // Accordion state: only one section can be open at a time
    var openSection by remember { mutableStateOf(BrowseSection.CATEGORIES) }

    val alphabet = ('A'..'Z').map { it.toString() }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .statusBarsPadding()
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Title
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = "Browse Recipes",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            // Categories Section Header
            item(span = { GridItemSpan(maxLineSpan) }) {
                CollapsibleHeader(
                    title = "By Category",
                    isExpanded = openSection == BrowseSection.CATEGORIES,
                    onToggle = {
                        openSection =
                            if (openSection == BrowseSection.CATEGORIES) BrowseSection.NONE
                            else BrowseSection.CATEGORIES
                    }
                )
            }

            if (openSection == BrowseSection.CATEGORIES) {
                if (isLoadingCategories) {
                    item(span = { GridItemSpan(maxLineSpan) }) { LoadingIndicator() }
                } else if (errorMessage != null) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        ErrorText(message = "Error loading categories")
                    }
                } else {
                    items(categories) { category ->
                        BrowseItem(
                            text = category.strCategory,
                            onClick = { onCategoryClick(category.strCategory) }
                        )
                    }
                }
            }

            // Cuisine Section Header
            item(span = { GridItemSpan(maxLineSpan) }) {
                Spacer(modifier = Modifier.height(8.dp))
                CollapsibleHeader(
                    title = "By Cuisine",
                    isExpanded = openSection == BrowseSection.CUISINE,
                    onToggle = {
                        openSection =
                            if (openSection == BrowseSection.CUISINE) BrowseSection.NONE
                            else BrowseSection.CUISINE
                    }
                )
            }

            if (openSection == BrowseSection.CUISINE) {
                if (isLoadingAreas) {
                    item(span = { GridItemSpan(maxLineSpan) }) { LoadingIndicator() }
                } else {
                    items(areas.filter { it.strArea?.lowercase() != "unknown" }) { area ->
                        CuisineItem(
                            area = area.strArea ?: "Unknown",
                            onClick = { onAreaClick(area.strArea ?: "") }
                        )
                    }
                }
            }

            // Letter Section Header
            item(span = { GridItemSpan(maxLineSpan) }) {
                Spacer(modifier = Modifier.height(8.dp))
                CollapsibleHeader(
                    title = "By First Letter",
                    isExpanded = openSection == BrowseSection.LETTERS,
                    onToggle = {
                        openSection =
                            if (openSection == BrowseSection.LETTERS) BrowseSection.NONE
                            else BrowseSection.LETTERS
                    }
                )
            }

            if (openSection == BrowseSection.LETTERS) {
                val chunkedLetters = alphabet.chunked(4)

                items(chunkedLetters, span = { GridItemSpan(maxLineSpan) }) { rowLetters ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowLetters.forEach { letter ->
                            Box(modifier = Modifier.weight(1f)) {
                                BrowseItem(text = letter, onClick = { onLetterClick(letter) })
                            }
                        }
                        repeat(4 - rowLetters.size) { Spacer(modifier = Modifier.weight(1f)) }
                    }
                }
            }

            // Bottom spacing
            item(span = { GridItemSpan(maxLineSpan) }) { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun CuisineItem(area: String, onClick: () -> Unit) {
    val countryCode = getCountryCode(area)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors =
            CardDefaults.cardColors(
                containerColor =
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (countryCode != null) {
                AsyncImage(
                    model = "https://flagcdn.com/w80/$countryCode.png",
                    contentDescription = "$area flag",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Fit
                )
            } else {
                Text(text = "🏳️", fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = area,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

fun getCountryCode(area: String): String? {
    return when (area.lowercase()) {
        // Adjective forms (MealDB strArea)
        "american" -> "us"
        "australian" -> "au"
        "austrian" -> "at"
        "belgian" -> "be"
        "brazilian" -> "br"
        "british" -> "gb"
        "bulgarian" -> "bg"
        "cambodian" -> "kh"
        "canadian" -> "ca"
        "chilean" -> "cl"
        "chinese" -> "cn"
        "croatian" -> "hr"
        "cuban" -> "cu"
        "cypriot" -> "cy"
        "czech" -> "cz"
        "danish" -> "dk"
        "dutch" -> "nl"
        "ecuadorian" -> "ec"
        "egyptian" -> "eg"
        "estonian" -> "ee"
        "filipino" -> "ph"
        "finnish" -> "fi"
        "french" -> "fr"
        "german" -> "de"
        "greek" -> "gr"
        "hungarian" -> "hu"
        "icelandic" -> "is"
        "indian" -> "in"
        "indonesian" -> "id"
        "irish" -> "ie"
        "israeli" -> "il"
        "italian" -> "it"
        "jamaican" -> "jm"
        "japanese" -> "jp"
        "kenyan" -> "ke"
        "korean" -> "kr"
        "kosovan" -> "xk"
        "latvian" -> "lv"
        "lithuanian" -> "lt"
        "luxembourgish" -> "lu"
        "macedonian" -> "mk"
        "malaysian" -> "my"
        "maltese" -> "mt"
        "mexican" -> "mx"
        "moldovan" -> "md"
        "moroccan" -> "ma"
        "norwegian" -> "no"
        "pakistani" -> "pk"
        "palestinian" -> "ps"
        "peruvian" -> "pe"
        "polish" -> "pl"
        "portuguese" -> "pt"
        "romanian" -> "ro"
        "russian" -> "ru"
        "salvadoran" -> "sv"
        "serbian" -> "rs"
        "singaporean" -> "sg"
        "slovak" -> "sk"
        "slovenian" -> "si"
        "spanish" -> "es"
        "swedish" -> "se"
        "swiss" -> "ch"
        "thai" -> "th"
        "tunisian" -> "tn"
        "turkish" -> "tr"
        "ukrainian" -> "ua"
        "vietnamese" -> "vn"
        // Country name forms (MealDB strCountry / alternate spellings)
        "united states",
        "usa" -> "us"

        "australia" -> "au"
        "austria" -> "at"
        "belgium" -> "be"
        "brazil" -> "br"
        "united kingdom", "uk", "england", "scotland", "wales" -> "gb"
        "bulgaria" -> "bg"
        "cambodia" -> "kh"
        "canada" -> "ca"
        "chile" -> "cl"
        "china" -> "cn"
        "croatia" -> "hr"
        "cuba" -> "cu"
        "cyprus" -> "cy"
        "czechia", "czech republic" -> "cz"
        "denmark" -> "dk"
        "netherlands", "holland" -> "nl"
        "ecuador" -> "ec"
        "egypt" -> "eg"
        "estonia" -> "ee"
        "philippines" -> "ph"
        "finland" -> "fi"
        "france" -> "fr"
        "germany" -> "de"
        "greece" -> "gr"
        "hungary" -> "hu"
        "iceland" -> "is"
        "india" -> "in"
        "indonesia" -> "id"
        "ireland" -> "ie"
        "israel" -> "il"
        "italy" -> "it"
        "jamaica" -> "jm"
        "japan" -> "jp"
        "kenya" -> "ke"
        "south korea", "korea" -> "kr"
        "kosovo" -> "xk"
        "latvia" -> "lv"
        "lithuania" -> "lt"
        "luxembourg" -> "lu"
        "north macedonia" -> "mk"
        "malaysia" -> "my"
        "malta" -> "mt"
        "mexico" -> "mx"
        "moldova" -> "md"
        "morocco" -> "ma"
        "norway" -> "no"
        "pakistan" -> "pk"
        "palestine" -> "ps"
        "peru" -> "pe"
        "poland" -> "pl"
        "portugal" -> "pt"
        "romania" -> "ro"
        "russia" -> "ru"
        "el salvador" -> "sv"
        "serbia" -> "rs"
        "singapore" -> "sg"
        "slovakia" -> "sk"
        "slovenia" -> "si"
        "spain" -> "es"
        "sweden" -> "se"
        "switzerland" -> "ch"
        "thailand" -> "th"
        "tunisia" -> "tn"
        "turkey", "türkiye" -> "tr"
        "ukraine" -> "ua"
        "vietnam" -> "vn"
        else -> null
    }
}

@Composable
fun CollapsibleHeader(title: String, isExpanded: Boolean, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 18.sp
        )
        Icon(
            imageVector =
                if (isExpanded) Icons.Default.KeyboardArrowUp
                else Icons.Default.KeyboardArrowDown,
            contentDescription = if (isExpanded) "Collapse" else "Expand",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun LoadingIndicator() {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(100.dp), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
fun ErrorText(message: String) {
    Text(
        text = message,
        color = MaterialTheme.colorScheme.error,
        modifier = Modifier.padding(vertical = 16.dp)
    )
}

@Composable
fun BrowseItem(text: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors =
            CardDefaults.cardColors(
                containerColor =
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}
