package com.example.moziwaregpt

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.moziwaregpt.ui.theme.Grey
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    var messages by remember { mutableStateOf(listOf<ChatMessage>()) }
    var inputText by remember { mutableStateOf("") }
    var isVoiceActive by remember { mutableStateOf(false) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    ModalNavigationDrawer(modifier = Modifier.imePadding(), // Keyboard Padding ,
        drawerState = drawerState, drawerContent = {
            ModalDrawerSheet {
                Column (modifier = Modifier
                    .fillMaxHeight()
                    .padding(16.dp), verticalArrangement = Arrangement.SpaceBetween) {
                    Button(onClick = {
                        navController.navigate(Screens.LOGIN.name)
                    }) {
                        Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "Log out")
                        Text(text = "Logout")
                    }
                }
            }
        },
        gesturesEnabled = drawerState.isOpen
    ) {

        Scaffold(
            modifier = Modifier
                .fillMaxSize(),

            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        contentColorFor(backgroundColor = Color.Transparent)
                    ),
                    title = {
                        Text(
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = TextStyle(
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            ),
                            text = "纽威服务大模型"
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(
                                modifier = Modifier.size(28.dp),
                                painter = painterResource(id = R.drawable.bars_menu_more),
                                contentDescription = "Menu Button",
                                tint = Color.Black
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            // TODO: Turns the sound of the AI on/off

                            // Changes Icon
                            isVoiceActive = !isVoiceActive
                        }) {
                            Icon(
                                modifier = Modifier.size(28.dp),
                                painter = painterResource(id = if (isVoiceActive) R.drawable.bars_menu_voice_open else R.drawable.bars_menu_voice_close),
                                contentDescription = if (isVoiceActive) "Voice Open" else "Voice Close"
                            )
                        }
                        IconButton(onClick = {
                            // TODO: SAVE THE CURRENT CHAT

                            // Clears the current chat
                            messages = emptyList()
                            inputText = ""
                        }) {
                            Icon(
                                modifier = Modifier.size(28.dp),
                                painter = painterResource(id = R.drawable.bars_menu_talk),
                                contentDescription = "New Chat"
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xff356bfd).copy(alpha = 0.1F),
                                Color(0xff77ddf7).copy(alpha = 0F)
                            )
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = innerPadding.calculateTopPadding() + 16.dp)
                ) {
                    ChatInterface(
                        messages = messages,
                        inputText = inputText,
                        onInputChange = { inputText = it },
                        onSendMessage = {
                            if (inputText.isNotBlank()) {
                                messages = messages + ChatMessage(inputText, true)
                                // TODO: Send message to backend here
                                inputText = ""
                                // TODO: ADD AI RESPONSE HERE
                                messages = messages + ChatMessage("DUMMY AI TEXT", false)
                            }
                        }
                    )
                }

            }
            }
        }
    }



@Composable
fun ChatInterface(
    messages: List<ChatMessage>,
    inputText: String,
    onInputChange: (String) -> Unit,
    onSendMessage: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                items(messages) { message ->
                    if (message.isUser) {
                        UserMessage(text = message.content)
                    } else {
                        AssistantMessage(text = message.content)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            ChatInputField(
                inputText = inputText,
                onInputChange = onInputChange,
                onSendMessage = onSendMessage
            )
        }
    }
}

@Composable
fun ChatInputField(
    inputText: String,
    onInputChange: (String) -> Unit,
    onSendMessage: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(12.dp)
            .padding(bottom = 21.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(28.dp),
                onClick = { /*TODO*/ }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_voice),
                    contentDescription = "Voice Input"
                )
            }

            TextField(
                value = inputText,
                onValueChange = onInputChange,
                modifier = Modifier
                    .weight(1f)
                    .clip(shape = RoundedCornerShape(32.dp)),
                placeholder = {
                    Text(text = "有问题尽管问我～", color = Color(0xff2d354b))
                },
                trailingIcon = {
                    IconButton(onClick = onSendMessage) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_talk_send),
                            contentDescription = "Send Button",
                            tint = if(inputText.isNotBlank()) Color(0xff004098) else Color.Gray
                        )
                    }
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Grey,
                    focusedContainerColor = Grey,
                    disabledIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )
        }
    }
}

@Composable
fun AssistantMessage(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp), horizontalArrangement = Arrangement.Start
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart = 8.dp,
                topEnd = 8.dp,
                bottomEnd = 8.dp,
                bottomStart = 1.dp
            ), color = Color.White
        ) {
            Text(text = text, modifier = Modifier.padding(6.dp), color = Color.Black)
        }
    }
}

@Composable
fun UserMessage(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart = 8.dp,
                topEnd = 8.dp,
                bottomStart = 8.dp,
                bottomEnd = 1.dp
            ), color = Color(0xff004098)
        ) {
            Text(text = text, modifier = Modifier.padding(6.dp), color = Color.White)
        }
    }
}

@Composable
fun QuestionScreen(viewModel: QuestionViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val apiResponse by viewModel.apiResponse.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val question by remember {
        mutableStateOf("")
    }
}
