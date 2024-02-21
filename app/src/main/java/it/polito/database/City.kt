package it.polito.database

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import it.polito.database.ui.theme.fontFamily

enum class City  {
    Bari, Bergamo, Bologna, Brescia, Como, Cremona, Catania, Ferrara,
    Milano, Napoli, Piacenza, Padova, Perugia, Parma, Pavia, Roma,
    Torino, Treviso, Udine, Varese, Venezia, Vicenza, Verona;
    companion object {
        private val cityMap = values().associateBy { it.name.toLowerCase() }

        fun fromString(city: String): City? {
            return cityMap[city.toLowerCase()]
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitySelection(
    selectedCity: City?,
    onCitySelected: (City) -> Unit
){
    val cities = enumValues<City>().toList()
    var isExpanded by remember { mutableStateOf(false) }
    var chosenCity = selectedCity.toString();
    if (selectedCity===null){
        chosenCity="Bari";
    }

    var textFiledSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (isExpanded){
        painterResource(id = R.drawable.frecciasopra)
    } else painterResource(id = R.drawable.frecciasotto)

    Column(modifier = Modifier.fillMaxWidth()){
        OutlinedTextField(
            shape = MaterialTheme.shapes.large,
            value = chosenCity,
            onValueChange = {},
            readOnly = false,
            trailingIcon = {
                Icon(icon , "", tint = Color.Black,
                    modifier = Modifier
                        .clickable { isExpanded = !isExpanded }
                        .size(16.dp))
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = if (isExpanded) MaterialTheme.colorScheme.onSecondary else Color.Black,
                textColor = MaterialTheme.colorScheme.onBackground
            ),
            textStyle = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFiledSize = coordinates.size.toSize()
                }

        )
        DropdownMenu(
            offset = DpOffset(0.dp, (4).dp),
            modifier = Modifier
                .width(with(LocalDensity.current) { textFiledSize.width.toDp() })
                .padding(vertical = 2.dp)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .verticalScroll(rememberScrollState())
                .height(400.dp)
                .border(1.dp, MaterialTheme.colorScheme.onPrimary),
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
        ) {
            for (index in cities.indices) {
                val city = cities[index]
                DropdownMenuItem(
                    text = {
                        Text(
                            text = city.toString(),
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.outline
                        ) },
                    onClick = {
                        onCitySelected(city)
                        isExpanded = false
                    }
                )
                if (index < cities.size - 1){
                    Divider(thickness = 2.dp, color = Color(0x1A000000))
                }

            }
        }
    }
}
