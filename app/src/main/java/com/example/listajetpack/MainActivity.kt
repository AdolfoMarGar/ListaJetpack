package com.example.listajetpack

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.listajetpack.util.SQLite.DBHelper
import com.example.listajetpack.util.item.NavItem
import com.example.listajetpack.util.modelo.Campeon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    lateinit var DB: DBHelper
    lateinit var campAux : Campeon
    var listaCampeon: ArrayList<Campeon> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DB = DBHelper(this, null)
        obtenerDatos()

        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {
                MainScreen()
            }
        }
    }

    fun obtenerDatos() {
        //obtenemos los datos de la bd
        val listaNom: ArrayList<String>
        val listaRol: ArrayList<String>
        val listaReg: ArrayList<String>
        val listaUrl: ArrayList<String>
        listaNom = DB.getNom()
        listaRol = DB.getRol()
        listaReg = DB.getReg()
        listaUrl = DB.getUrl()
        for (i in listaNom.indices) {
            campAux = Campeon(listaNom[i],listaRol[i],listaReg[i],listaUrl[i])
            listaCampeon.add(campAux)
        }
    }

    private fun sqlInsert(txtNom: String, txtRol: String, txtReg: String, txtUrl: String) {
        DB.insert(txtNom,txtRol,txtReg,txtUrl)
    }

    private fun borrarCampeon(txtNom: String) {
        DB.delete(txtNom)
        var pos : Int = -1
        for (i in listaCampeon.indices) {
            if (listaCampeon.get(i).nom.equals(txtNom)){
                pos=i

            }
        }
        listaCampeon.removeAt(pos)
    }

    fun Toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    @Composable
    fun TopBar(scope: CoroutineScope, scaffoldState: ScaffoldState) {

        TopAppBar(
            title = { Text(text = "App Jetpack Compose 1ª Evaluación", fontSize = 18.sp) },
            navigationIcon = {
                IconButton(onClick = {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                }){
                    Icon(Icons.Filled.Menu, "")
                }
            },
            backgroundColor = Color.Blue,
            contentColor = Color.White
        )
    }

    @Composable
    fun Drawer(scope: CoroutineScope, scaffoldState: ScaffoldState, navController: NavController) {

        val items = listOf(
            NavItem.Home,
            NavItem.Anadir,
            NavItem.Borrar,
        )

        Column(
            modifier = Modifier
                .background(color = Color.LightGray)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(125.dp)
                    .background(Color.Blue),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.casa_24),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(Color.LightGray),
                    modifier = Modifier
                        .height(120.dp)
                        .fillMaxWidth()
                        .padding(15.dp)
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(7.dp)
            )

            val navEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navEntry?.destination?.route

            items.forEach {
                items ->DrawerItem(
                    item = items,
                    selected = currentRoute == items.label,
                    onItemClick = {
                        navController.navigate(items.label) {
                            navController.graph.startDestinationRoute?.let {
                                route ->popUpTo(route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                        scope.launch {
                            scaffoldState.drawerState.close()
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.weight(1f))

        }
    }

    @Composable
    fun DrawerItem(item: NavItem, selected: Boolean, onItemClick: (NavItem) -> Unit) {
        val background = if (selected) Color.Cyan else Color.Transparent
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onItemClick(item) }
                .height(50.dp)
                .background(background)
                .padding(start = 12.dp)
        ) {
            Image(
                painter = painterResource(id = item.img),
                contentDescription = item.label,
                colorFilter = ColorFilter.tint(Color.White),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(22.dp)
                    .width(22.dp)
            )
            Spacer(modifier = Modifier.width(7.dp))
            Text(
                text = item.label,
                fontSize = 14.sp,
                color = Color.Black
            )
        }
    }

    @Composable
    fun Navigation(navController: NavHostController) {
        NavHost(navController, startDestination = NavItem.Home.label) {
            composable(NavItem.Home.label) {
                HomeScreen()
            }
            composable(NavItem.Anadir.label) {
                AnadirScreen()
            }
            composable(NavItem.Borrar.label) {
                BorrarScreen()
            }
        }
    }

    @Composable
    fun HomeScreen(){
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Cyan,
                            Color.Blue
                        )
                    )
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            LazyColumn(
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(22.dp),
                horizontalAlignment = Alignment.Start
            ){
                items(listaCampeon) { campeon ->
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ){
                        Box(
                            modifier = Modifier
                                .border(5.dp, Color.Black)
                                .fillMaxHeight()
                                .fillMaxWidth(),
                        ){
                            CargarURL(url = campeon.url)
                        }
                    }
                    Column {
                        Row (modifier = Modifier
                            .fillMaxWidth()
                            .height(25.dp)
                        ){
                            Text(
                                text = campeon.nom,
                                color = Color.Black,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp, 0.dp, 0.dp, 0.dp)
                                    .height(40.dp)
                                    .size(30.dp),
                                style = TextStyle(
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun MainScreen() {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scaffoldState = rememberScaffoldState(drawerState)
        val scope = rememberCoroutineScope()
        val navContr = rememberNavController()

        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopBar(
                    scope = scope,
                    scaffoldState = scaffoldState
                )
             },
            drawerContent = {
                Drawer(
                    scope = scope,
                    scaffoldState = scaffoldState,
                    navController = navContr
                )
            }
        ){
            Navigation(navController = navContr)
        }
    }

    @Composable
    private fun AnadirScreen() {
        var txtNom by rememberSaveable { mutableStateOf("") }
        var txtRol by rememberSaveable { mutableStateOf("") }
        var txtReg by rememberSaveable { mutableStateOf("") }
        var txtUrl by rememberSaveable { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Blue,
                            Color.Cyan
                        )
                    )
                )
        ){
            TextField(
                value = txtNom,
                onValueChange = {
                    nuevo -> txtNom = nuevo
                },
                label = {
                    Text(text = "Nombre")
                },
                modifier = Modifier
                    .padding(10.dp, 30.dp, 10.dp, 0.dp)
                    .align(Alignment.CenterHorizontally)
                    .background(Color.LightGray),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                textStyle = TextStyle(textAlign = TextAlign.Left)
            )
            TextField(
                value = txtRol,
                onValueChange = {
                        nuevo -> txtRol = nuevo
                },
                label = {
                    Text(text = "Rol")
                },
                modifier = Modifier
                    .padding(10.dp, 30.dp, 10.dp, 0.dp)
                    .align(Alignment.CenterHorizontally)
                    .background(Color.LightGray),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                textStyle = TextStyle(textAlign = TextAlign.Left)
            )
            TextField(
                value = txtReg,
                onValueChange = {
                        nuevo -> txtReg = nuevo
                },
                label = {
                    Text(text = "Región")
                },
                modifier = Modifier
                    .padding(10.dp, 30.dp, 10.dp, 0.dp)
                    .align(Alignment.CenterHorizontally)
                    .background(Color.LightGray),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                textStyle = TextStyle(textAlign = TextAlign.Left)
            )
            TextField(value = txtUrl,
                onValueChange = {
                    nuevo -> txtUrl = nuevo
                },
                label = {
                    Text(text = "Url de la imagen")
                },
                modifier = Modifier
                    .padding(10.dp, 30.dp, 10.dp, 0.dp)
                    .align(Alignment.CenterHorizontally)
                    .background(Color.LightGray),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                textStyle = TextStyle(textAlign = TextAlign.Left)
            )
            Spacer(Modifier.height(40.dp))
            Row(
                modifier = Modifier.fillMaxSize()
            ){
                Spacer(Modifier.width(65.dp))
                Button(
                    modifier = Modifier.size(width = 100.dp, height = 50.dp),
                    onClick = {
                        sqlInsert(txtNom, txtRol, txtReg, txtUrl)
                        listaCampeon.add(Campeon(txtNom,txtRol,txtReg,txtUrl))
                        txtNom = ""
                        txtRol = ""
                        txtReg = ""
                        txtUrl = ""
                        Toast("Campeon añadido")
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Black
                    )
                ){
                    Text(
                        text = "Añadir",
                        color = Color.Black
                    )
                }
            }
        }
    }

    @Composable
    fun BorrarScreen() {
        var txtNom by rememberSaveable { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Blue,
                            Color.Cyan
                        )
                    )
                )
        ){
            TextField(
                value = txtNom,
                onValueChange = {
                    nuevo ->
                    txtNom = nuevo
                },
                label = {
                    Text(text = "Introduce el nombre:")
                },
                modifier = Modifier
                    .padding(10.dp, 30.dp, 10.dp, 0.dp)
                    .align(Alignment.CenterHorizontally)
                    .background(Color.LightGray),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                textStyle = TextStyle(textAlign = TextAlign.Left)
            )
            Spacer(Modifier.height(40.dp))
            Row(
                modifier = Modifier
                    .fillMaxSize()
            ){
                Spacer(Modifier.width(70.dp))
                Button(
                    modifier = Modifier
                        .background(Color.Black, RoundedCornerShape(100.dp))
                        .size(width = 100.dp, height = 50.dp),
                    onClick = {
                        borrarCampeon(txtNom)
                        txtNom = ""
                        Toast("Campeón eliminado.")
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Black
                    )
                ){
                    Text(
                        text = "Borrar",
                        color = Color.Black
                    )
                }
            }
        }
    }

    @Composable
    fun CargarURL(url: String) {
        Image(
            painter = rememberImagePainter(url),
            contentDescription = "imgagen : "+url,
            contentScale = ContentScale.FillWidth,

            modifier = Modifier
                .fillMaxWidth()
                .height(225.dp)
                .padding(1.dp)
        )
    }

}