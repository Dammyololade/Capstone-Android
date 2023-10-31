@file:OptIn(ExperimentalGlideComposeApi::class)

package com.example.capstone_android.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.example.capstone_android.R
import com.example.capstone_android.database.AppDatabase
import com.example.capstone_android.database.MenuItemRoom
import com.example.capstone_android.ui.theme.accent
import com.example.capstone_android.ui.theme.primary
import com.example.capstone_android.ui.theme.white

@Composable
fun HomeScreen(database: AppDatabase, navController: NavController?) {
    val databaseMenuItems by database.menuItemDao().getAll().observeAsState(emptyList())


    Surface(modifier = Modifier.fillMaxSize()) {
        Column {
            ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                val (logo, profile) = createRefs()

                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .constrainAs(logo) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .size(height = 80.dp, width = 160.dp)
                )

                Image(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = "Profile",
                    modifier = Modifier
                        .constrainAs(profile) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                        }
                        .padding(16.dp)
                        .size(height = 40.dp, width = 40.dp)
                        .clip(shape = CircleShape)
                        .background(color = Color.Transparent)
                        .clickable {
                            navController?.navigate(Profile.route)
                        }
                )

            }

            val items = remember { mutableStateOf(databaseMenuItems) }
            val searchRef = remember { mutableStateOf("") }
            val filterRef = remember { mutableStateOf("") }

            Header(searchRef = searchRef.value) {
                searchRef.value = it
                items.value = getItems(databaseMenuItems, it, filterRef.value)
            }
            FilterSection {
                filterRef.value = it
                items.value = getItems(databaseMenuItems, searchRef.value, filterRef.value)
            }
            MenuItemsList(
                items = if (items.value.isEmpty() && searchRef.value.isEmpty() && filterRef.value.isEmpty()) databaseMenuItems else items.value
            )


        }
    }
}

fun getItems(
    dataBaseMenuItems: List<MenuItemRoom>,
    search: String,
    filter: String
): List<MenuItemRoom> {
    var menuItems = dataBaseMenuItems;
    if (search.isNotEmpty()) {
        menuItems = menuItems.filter { menuItem ->
            menuItem.title.contains(search, ignoreCase = true)
        }
    }
    if (filter.isNotEmpty()) {
        menuItems = menuItems.filter { menuItem ->
            menuItem.category.contains(filter, ignoreCase = true)
        }
    }
    return menuItems
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(searchRef: String, onSearch: (String) -> Unit) {
    Box(
        modifier = Modifier
            .background(color = primary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                "Little Lemon", style = TextStyle(
                    color = accent,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Medium
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Chicago", style = TextStyle(
                            color = white,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Spacer(modifier = Modifier.size(12.dp))
                    Text(
                        "We are a family-owned Mediterranean restaurant, focused on traditional recipes served with a modern twist",
                        style = TextStyle(
                            color = white,
                            fontSize = 16.sp
                        )
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.hero_image),
                    contentDescription = "Hero",
                    modifier = Modifier
                        .size(height = 120.dp, width = 120.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.size(12.dp))

            TextField(
                value = searchRef,
                onValueChange = {
                    onSearch(it)
                },
                modifier = Modifier
                    .fillMaxWidth(),
                placeholder = { Text("Enter Search phrase") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "icon")
                },
            )

        }
    }
}

@Composable
fun FilterSection(onFilter: (String) -> Unit) {
    val filters = listOf("Starters", "Mains", "Drinks", "Dessert")
    val selectedFilter = remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.size(12.dp))
        Text(
            text = "ORDER FOR DELIVERY",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        )
        Spacer(modifier = Modifier.size(16.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(filters.size) { item ->
                Card(
                    colors = if (selectedFilter.value == filters[item]) {
                        CardDefaults.cardColors(
                            containerColor = primary,
                        )
                    } else {
                        CardDefaults.cardColors()
                    },
                    modifier = Modifier.clickable {
                        if (selectedFilter.value == filters[item]) {
                            selectedFilter.value = ""
                            onFilter("")
                        } else {
                            selectedFilter.value = filters[item]
                            onFilter(filters[item])
                        }

                    }
                ) {
                    Text(text = filters[item], modifier = Modifier.padding(20.dp, 12.dp))
                }
            }
        }
    }
}

@Composable
private fun MenuItemsList(items: List<MenuItemRoom>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .padding(vertical = 20.dp, horizontal = 16.dp)
    ) {
        items(
            items = items,
            itemContent = { menuItem ->
                Column {
                    Text(
                        text = menuItem.title,
                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Medium)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 4.dp)
                        ) {
                            Text(
                                menuItem.description,
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            )
                            Text(
                                modifier = Modifier
                                    .padding(top = 4.dp),
                                textAlign = TextAlign.Right,
                                text = "$" + "%.2f".format(menuItem.price),
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(menuItem.image)
                                .crossfade(true)
                                .build(),
                            placeholder = painterResource(android.R.drawable.ic_menu_report_image),
                            contentDescription = "image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(80.dp),
                        )
                    }
                    Divider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        thickness = 1.dp,
                    )
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    Surface {
        Column {
            Header("") {}
            FilterSection() {}
            MenuItemsList(
                items = listOf(
                    MenuItemRoom(
                        1,
                        "Main Dish Cat",
                        1000.0,
                        "https://res.cloudinary.com/dutzkmmox/image/upload/v1689703647/knitfocus/3_gfobyw.png",
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                        "Dessert"
                    )
                )
            )
        }
    }
}