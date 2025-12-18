package com.one.yoread

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt
import android.widget.Toast
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.coroutineScope
import kotlin.math.absoluteValue
import kotlin.random.Random

/**
 * 卡片数据类
 * 用于传递卡片信息到详情页
 */
data class CardData(
    val title: String,
    val author: String,
    val coverImage: Int
)

/**
 * 章节数据类
 * 用于详情页右侧列表显示
 */
data class ChapterData(
    val title: String,
    val chapterCount: Int
)

/**
 * 主页面
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                // 欢迎页和主界面切换
                AppContent()
            }
        }
    }
}

/**
 * 应用主内容
 * 控制欢迎页和主界面的显示切换
 * 包含全局悬浮球
 */
@Composable
fun AppContent() {
    var showSplash by remember { mutableStateOf(true) }
    
    Box(modifier = Modifier.fillMaxSize()) {
        // 主内容
        if (showSplash) {
            SplashScreen(
                onSplashEnd = { showSplash = false }
            )
        } else {
            MainScreen()
        }
        
        // 全局悬浮球：显示在最上层
        FloatingBall()
    }
}

/**
 * 欢迎页组件
 * 显示随机渐变背景和随机激励话语，3秒后自动跳转到主界面
 * @param onSplashEnd 欢迎页结束回调
 */
@Composable
fun SplashScreen(onSplashEnd: () -> Unit) {
    // 随机激励话语列表
    val motivationalQuotes = remember {
        listOf(
            "每一天都是新的开始",
            "坚持就是胜利",
            "相信自己，你能行",
            "努力不会被辜负",
            "今天的努力，明天的收获",
            "梦想不会发光，发光的是追梦的你",
            "越努力，越幸运",
            "成功属于永不放弃的人",
            "做最好的自己",
            "行动胜过一切空想",
            "每一次努力都是成长",
            "坚持到底，就是胜利",
            "相信自己，无限可能",
            "今天的汗水，明天的辉煌",
            "努力的人，运气不会太差"
        )
    }
    
    // 随机选择一句激励话语
    val randomQuote = remember {
        motivationalQuotes[Random.nextInt(motivationalQuotes.size)]
    }
    
    // 生成随机渐变浅色背景
    val gradientColors = remember {
        // 浅色系颜色列表
        val lightColors = listOf(
            Color(0xFFFFE5E5), // 浅粉红
            Color(0xFFE5F3FF), // 浅蓝色
            Color(0xFFE5FFE5), // 浅绿色
            Color(0xFFFFF5E5), // 浅橙色
            Color(0xFFF0E5FF), // 浅紫色
            Color(0xFFFFE5F0), // 浅玫瑰色
            Color(0xFFE5FFFF), // 浅青色
            Color(0xFFFFF0E5), // 浅桃色
            Color(0xFFE5E5FF), // 浅靛蓝色
            Color(0xFFFFF5F5)  // 浅米色
        )
        
        // 随机选择两个颜色作为渐变的起点和终点
        val color1 = lightColors[Random.nextInt(lightColors.size)]
        val color2 = lightColors[Random.nextInt(lightColors.size)]
        
        // 创建渐变，从左上到右下
        Brush.linearGradient(
            colors = listOf(color1, color2),
            start = Offset(0f, 0f),
            end = Offset(1000f, 1000f)
        )
    }
    
    // 3秒后自动跳转到主界面
    LaunchedEffect(Unit) {
        delay(3000) // 3秒延迟
        onSplashEnd()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientColors),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = randomQuote,
            fontSize = 52.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333), // 深灰色文字，在浅色背景上清晰可见
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
        )
    }
}

/**
 * 主界面组合函数
 * 包含顶部导航栏和内容区域
 * 支持点击tab切换和左右滑动切换
 */
@Composable
fun MainScreen() {
    // Tab 页面数量
    val tabCount = 2
    // 使用 PagerState 管理页面状态，默认显示第一个页面（发现tab，索引0）
    val pagerState = rememberPagerState(initialPage = 0) { tabCount }
    // 记录当前选中的 tab 索引，用于顶部导航栏显示
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val coroutineScope = rememberCoroutineScope()
    
    // 详情页状态管理
    var selectedCardData by remember { mutableStateOf<CardData?>(null) }

    // 监听 PagerState 的变化，同步到 selectedTabIndex
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            selectedTabIndex = page
        }
    }

    // 根据是否显示详情页来决定显示内容
    if (selectedCardData != null) {
        // 显示详情页
        DetailScreen(
            cardData = selectedCardData!!,
            onBack = { selectedCardData = null }
        )
    } else {
        // 首页整体背景色：灰黑色
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF2C2C2C)) // 灰黑色背景
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
            // 顶部导航栏区域（宽度撑满、高度80px）
            TopNavigationBar(
                selectedTabIndex = selectedTabIndex,
                onTabSelected = { index ->
                    // 点击tab时，切换到对应的页面
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )

                // Tab 关联的内容显示区域（撑满剩余屏幕区域）
                // 使用 HorizontalPager 实现左右滑动切换
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f) // 占据剩余空间
                ) { page ->
                    // 根据页面索引显示不同的内容
                    TabContentPage(
                        pageIndex = page,
                        onCardClick = { cardData ->
                            selectedCardData = cardData
                        }
                    )
                }
            }
        }
    }
}

/**
 * 顶部导航栏
 * 使用 ConstraintLayout 实现精确布局，兼容多设备尺寸
 * @param selectedTabIndex 当前选中的 tab 索引
 * @param onTabSelected tab 选中回调
 */
@Composable
fun TopNavigationBar(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    // 控制弹框显示/隐藏的状态
    var showDialog by remember { mutableIntStateOf(0) }
    val showDialogBoolean = showDialog > 0
    
    // 显示弹框
    if (showDialogBoolean) {
        LogoDialog(
            onDismiss = { showDialog = 0 }
        )
    }
    // 导航栏高度：80px（使用dp单位，自适应不同屏幕密度）
    val navigationBarHeight = 80.dp
    // Logo 尺寸
    val logoSize = 60.dp
    // 外边距
    val margin = 20.dp
    // Tab 之间的间隔
    val tabSpacing = 60.dp

    // 导航栏背景色：使用灰黑色背景，与首页整体背景一致
    Surface(
        modifier = Modifier
            .fillMaxWidth() // 宽度撑满
            .height(navigationBarHeight), // 高度80px
        color = Color(0xFF2C2C2C) // 灰黑色背景，与首页整体背景一致
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            // 创建约束引用
            val (logoRef, discoverTabRef, recommendTabRef, loginIconRef, centerRef) = createRefs()
            
            // 创建一个不可见的居中参考点，用于让两个tab整体居中
            Box(
                modifier = Modifier
                    .size(1.dp) // 不可见的参考点
                    .constrainAs(centerRef) {
                        // 这个参考点在屏幕正中心
                        centerHorizontallyTo(parent)
                        centerVerticallyTo(parent)
                    }
            )

            // 左上角 Logo（60px * 60px，外边距 20px）
            // 使用 drawable 中的图片资源（支持 PNG、JPG、WEBP 格式）
            // 添加点击事件，点击后弹出弹框
            Box(
                modifier = Modifier
                    .size(logoSize) // 60px * 60px
                    .background(
                        color = Color(0xFFEC0934), // 浅蓝色背景
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { showDialog = 1 } // 点击logo显示弹框
                    .constrainAs(logoRef) {
                        // 约束到父布局的左上角，外边距20px
                        start.linkTo(parent.start, margin = margin)
                        top.linkTo(parent.top, margin = margin)
                        bottom.linkTo(parent.bottom, margin = margin)
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.mipmap.main_logo_w),
                    contentDescription = "Logo",
                    modifier = Modifier.size(logoSize - 8.dp), // 稍微小一点，留出边框空间
                    contentScale = ContentScale.Fit
                )
            }

            // "发现" Tab（与左侧 logo 垂直对齐，整体居中在屏幕中间）
            // 使用居中参考点让两个 tab 的中心点整体居中
            TabButton(
                text = "发现",
                isSelected = selectedTabIndex == 0,
                onClick = { onTabSelected(0) },
                modifier = Modifier.constrainAs(discoverTabRef) {
                    // 与 logo 垂直对齐（顶部和底部对齐）
                    top.linkTo(logoRef.top)
                    bottom.linkTo(logoRef.bottom)
                    // 让"发现"tab的结束位置在中心参考点左侧（tab间距/2），使两个tab的中心点居中
                    end.linkTo(centerRef.start, margin = tabSpacing / 2)
                }
            )

            // "推荐" Tab
            TabButton(
                text = "推荐",
                isSelected = selectedTabIndex == 1,
                onClick = { onTabSelected(1) },
                modifier = Modifier.constrainAs(recommendTabRef) {
                    // 与"发现" tab 垂直对齐
                    top.linkTo(discoverTabRef.top)
                    bottom.linkTo(discoverTabRef.bottom)
                    // 在"发现" tab 右侧
                    start.linkTo(discoverTabRef.end, margin = tabSpacing)
                }
            )

            // 右上角登录图标（60px * 60px，外边距 20px）
            Box(
                modifier = Modifier
                    .size(logoSize) // 60px * 60px
                    .background(
                        color = Color(0xFFFFE0B2), // 浅橙色背景
                        shape = RoundedCornerShape(8.dp)
                    )
                    .constrainAs(loginIconRef) {
                        // 约束到父布局的右上角，外边距20px
                        end.linkTo(parent.end, margin = margin)
                        top.linkTo(parent.top, margin = margin)
                        bottom.linkTo(parent.bottom, margin = margin)
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "登录",
                    modifier = Modifier.size(logoSize - 20.dp), // 图标稍微小一点
                    tint = Color(0xFFE65100) // 深橙色图标
                )
            }
        }
    }
}

/**
 * Tab 按钮组件
 * 选中和未选中状态有明显的颜色区分
 * @param text 显示的文本
 * @param isSelected 是否选中
 * @param onClick 点击回调
 * @param modifier 修饰符
 */
@Composable
fun TabButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clickable { onClick() }
//            .background(
//                // 选中时使用蓝色背景，未选中时使用更明显的灰色背景，确保可见性
//                color = if (isSelected) Color(0xFF2196F3) else Color(0xFFBDBDBD),
//                shape = RoundedCornerShape(8.dp)
//            )
            .padding(horizontal = 20.dp, vertical = 10.dp), // 增加内边距，让按钮更明显
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 24.sp, // 稍微增大字体
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold, // 未选中也使用半粗体，更明显
            // 选中时使用白色文字，未选中时使用深灰色文字，颜色区分明显
            color = if (isSelected) Color.White else Color(0xFF9B9797) // 使用更深的颜色
        )
    }
}

/**
 * Tab 关联的内容页面
 * 根据页面索引显示不同的内容
 * 每个页面撑满整个显示区域
 * @param pageIndex 页面索引（0: 发现, 1: 推荐）
 * @param onCardClick 卡片点击回调
 */
@Composable
fun TabContentPage(
    pageIndex: Int,
    onCardClick: (CardData) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize() // 撑满整个显示区域
            .background(Color(0xFF2C2C2C)), // 灰黑色背景，与首页整体背景一致
        contentAlignment = Alignment.Center
    ) {
        // 根据页面索引显示不同的内容
        when (pageIndex) {
             0 -> {
                // "发现" tab 的内容区域：垂直滑动布局
                DiscoverContent(onCardClick = onCardClick)
            }
            1 -> {
                // "推荐" tab 的内容区域
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "推荐",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White // 白色文字，在灰黑色背景上更清晰
                    )
                    Text(
                        text = "这是推荐页面的内容区域",
                        fontSize = 16.sp,
                        color = Color(0xFFCCCCCC), // 浅灰色文字，在灰黑色背景上更清晰
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

/**
 * 发现页内容区域
 * 实现垂直滑动布局，包含banner轮播图和分类列表
 * @param onCardClick 卡片点击回调
 */
@Composable
fun DiscoverContent(onCardClick: (CardData) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        item {
            // 顶部区域：水平平分两份，高度300dp
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                // 左边区域：banner轮播图（占50%宽度）
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    BannerCarousel()
                }
                
                // 右边区域：分类列表（占50%宽度）
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    CategoryList()
                }
            }
        }
        
        // 可以继续添加其他内容项
        // 每5个item组成一行，平分整体宽度
        // 总共20个item，分成4行
        val totalItems = 66
        val itemsPerRow = 8
        val totalRows = (totalItems + itemsPerRow - 1) / itemsPerRow // 向上取整
        
        items(totalRows) { rowIndex ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // 每行显示5个item
                repeat(itemsPerRow) { colIndex ->
                    val itemIndex = rowIndex * itemsPerRow + colIndex
                    if (itemIndex < totalItems) {
                        // 使用weight来平分宽度
                        // 封面图片列表（循环使用）
                        val coverImages = listOf(
                            R.drawable.nice,
                            R.drawable.nice_a,
                            R.drawable.nice_b
                        )
                        val cardData = CardData(
                            title = "大话降龙 ${itemIndex + 1}",
                            author = "作者 ${itemIndex + 1}",
                            coverImage = coverImages[itemIndex % coverImages.size]
                        )
                        CardItem(
                            title = cardData.title,
                            author = cardData.author,
                            coverImage = cardData.coverImage,
                            modifier = Modifier.weight(1f),
                            onClick = { onCardClick(cardData) }
                        )
                    } else {
                        // 如果最后一行不足5个，用Spacer填充
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

/**
 * Banner 轮播图组件
 * 支持无限循环滑动、自动轮播（每3秒）、手动滑动、定位点指示器
 */
@Composable
fun BannerCarousel() {
    // Banner 图片列表（示例数据，实际应该从数据源获取）
    val bannerImages = remember {
        listOf(
            R.drawable.nice,
            R.drawable.nice_a,
            R.drawable.nice_b
        )
    }
    
    val bannerCount = bannerImages.size
    // 使用一个很大的pageCount来实现无限循环效果
    // 从中间开始，确保可以向前和向后滑动
    val initialPage = 1000 // 从中间开始，确保可以循环
    val pageCount = Int.MAX_VALUE
    
    // 使用 PagerState 管理轮播状态，支持无限循环
    val pagerState = rememberPagerState(initialPage = initialPage) { pageCount }
    
    // 计算当前实际显示的图片索引（通过取模实现循环）
    val currentImageIndex = pagerState.currentPage % bannerCount
    
    // 自动轮播逻辑：每3秒切换到下一页
    // 当用户手动滑动时，isScrollInProgress 为 true，暂停自动轮播
    LaunchedEffect(pagerState) {
        while (true) {
            delay(3000) // 3秒延迟
            // 如果用户正在手动滑动，等待滑动完成
            if (!pagerState.isScrollInProgress) {
                // 直接切换到下一页，由于pageCount很大，可以实现无限循环
                pagerState.animateScrollToPage(pagerState.currentPage + 1)
            }
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp), // 10dp外边距
        contentAlignment = Alignment.BottomCenter
    ) {
        // 轮播图：添加15dp圆角
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            // 通过取模计算实际显示的图片索引，实现循环效果
            val imageIndex = page % bannerCount
            Image(
                painter = painterResource(id = bannerImages[imageIndex]),
                contentDescription = "Banner ${imageIndex + 1}",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(15.dp)), // 15dp圆角，使用clip实现
                contentScale = ContentScale.Crop
            )
        }
        
        // 定位点指示器（显示在轮播图下方）
        Row(
            modifier = Modifier
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            repeat(bannerCount) { index ->
                Box(
                    modifier = Modifier
                        .size(
                            width = if (currentImageIndex == index) 20.dp else 8.dp,
                            height = 8.dp
                        )
                        .background(
                            color = if (currentImageIndex == index) 
                                Color.White 
                            else 
                                Color.White.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(4.dp)
                        )
                )
            }
        }
    }
}

/**
 * 分类列表组件
 * 分上下两行，每行4个类型，存在间隔，整体居中显示
 */
@Composable
fun CategoryList() {
    // 分类数据（示例数据，实际应该从数据源获取）
    val categories = remember {
        listOf(
            "类型1", "类型2", "类型3", "类型4",
            "类型5", "类型6", "类型7", "类型8"
        )
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 第一行：4个分类
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            categories.take(4).forEach { category ->
                CategoryItem(text = category)
            }
        }
        
        // 行间距
        Spacer(modifier = Modifier.height(16.dp))
        
        // 第二行：4个分类
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            categories.drop(4).take(4).forEach { category ->
                CategoryItem(text = category)
            }
        }
    }
}

/**
 * 分类项组件
 * @param text 分类名称
 */
@Composable
fun CategoryItem(text: String) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .background(
                color = Color(0xFF3C3C3C),
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            color = Color.White,
            maxLines = 1
        )
    }
}

/**
 * 卡片项组件
 * 平分整体宽度5份，外边距15dp，高度为宽度的1.2倍，圆角10dp，随机背景色
 * 包含封面、标题、作者信息，垂直布局
 * @param title 卡片标题
 * @param author 作者名称
 * @param coverImage 封面图片资源ID
 * @param modifier 修饰符
 * @param onClick 点击回调
 */
@Composable
fun CardItem(
    title: String,
    author: String,
    coverImage: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    // 生成随机背景色（基于title的hashCode，确保相同title颜色一致）
    val backgroundColor = remember(title) {
        val colors = listOf(
            Color(0xFFFF6B6B), // 红色
            Color(0xFF4ECDC4), // 青色
            Color(0xFFFFE66D), // 黄色
            Color(0xFF95E1D3), // 浅绿色
            Color(0xFFF38181), // 粉红色
            Color(0xFFAA96DA), // 紫色
            Color(0xFFFCBAD3), // 粉色
            Color(0xFFA8E6CF), // 浅绿
            Color(0xFFFFD3A5), // 橙色
            Color(0xFFA8D8EA)  // 浅蓝
        )
        colors[title.hashCode().absoluteValue % colors.size]
    }
    
    // 使用BoxWithConstraints来获取实际宽度，计算高度
    BoxWithConstraints(
        modifier = modifier
    ) {
        // 计算高度：宽度 * 1.5
        val cardWidth = maxWidth - 0.dp // 减去外边距已经在Row中处理
        val cardHeight = cardWidth * 1.5f
        
        // 自适应计算：基于卡片宽度计算字体大小和间距
        // 基准宽度：60dp（参考值，可根据实际情况调整）
        val baseWidth = 60.dp
        // 计算缩放因子（限制在0.7到1.5之间，避免字体过大或过小）
        val scaleFactor = (cardWidth / baseWidth).coerceIn(0.7f, 1.5f)
        
        // 基准字体大小和间距
        val baseTitleFontSize = 12.sp
        val baseAuthorFontSize = 10.sp
        val baseTitleSpacing = 6.dp // 标题与封面的间距
        val baseAuthorSpacing = 2.dp // 作者与标题的间距
        val baseCoverTopPadding = 4.dp // 封面顶部间距
        
        // 根据缩放因子计算实际字体大小和间距
        val titleFontSize = (baseTitleFontSize.value * scaleFactor).sp
        val authorFontSize = (baseAuthorFontSize.value * scaleFactor).sp
        val titleSpacing = baseTitleSpacing * scaleFactor
        val authorSpacing = baseAuthorSpacing * scaleFactor
        val coverTopPadding = baseCoverTopPadding * scaleFactor
        
        Box(
            modifier = Modifier
                .width(cardWidth)
                .height(cardHeight)
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(10.dp) // 10dp圆角
                )
                .clickable { onClick() } // 添加点击事件
                .padding(8.dp * scaleFactor), // 整体内边距也根据缩放因子调整
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 封面：宽度占卡片宽度的90%，高度占卡片高度的70%，水平居中，向上靠近
                val coverWidth = cardWidth * 0.9f
                val coverHeight = cardHeight * 0.75f
                
                Image(
                    painter = painterResource(id = coverImage),
                    contentDescription = "封面",
                    modifier = Modifier
                        .width(coverWidth)
                        .height(coverHeight)
                        .padding(top = coverTopPadding) // 顶部外边距，根据缩放因子自适应
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                
                // 标题：显示在封面下方，靠左显示，字体大小和间距自适应
                Text(
                    text = title,
                    fontSize = titleFontSize, // 根据卡片宽度自适应
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth(0.9f) // 宽度与封面一致
                        .padding(top = titleSpacing) // 与封面的间距，根据缩放因子自适应
                        .padding(horizontal = 0.dp), // 水平对齐封面
                    textAlign = TextAlign.Left
                )
                
                // 作者：显示在标题下方，靠左显示，字体大小和间距自适应
                Text(
                    text = author,
                    fontSize = authorFontSize, // 根据卡片宽度自适应
                    color = Color.Black.copy(alpha = 0.8f),
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth(0.9f) // 宽度与封面一致
                        .padding(top = authorSpacing) // 与标题的间距，根据缩放因子自适应
                        .padding(horizontal = 0.dp), // 水平对齐封面
                    textAlign = TextAlign.Left
                )
            }
        }
    }
}

/**
 * Logo 点击后的弹框组件
 * 弹框尺寸：400dp * 300dp，圆角15dp，背景浅白色
 * 点击弹框外区域可关闭弹框
 * @param onDismiss 关闭弹框的回调
 */
@Composable
fun LogoDialog(onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss, // 点击外部区域关闭弹框
        properties = DialogProperties(
            dismissOnBackPress = true, // 按返回键关闭
            dismissOnClickOutside = true // 点击外部区域关闭
        )
    ) {
        // 弹框背景：透明，让外部区域可见
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onDismiss() }, // 点击外部区域关闭
            contentAlignment = Alignment.Center
        ) {
            // 弹框内容：200dp * 150dp，圆角15dp，浅白色背景
            Box(
                modifier = Modifier
                    .size(width = 400.dp, height = 300.dp)
                    .background(
                        color = Color(0xFFFAFAFA), // 浅白色背景
                        shape = RoundedCornerShape(15.dp)
                    )
                    .clickable { }, // 阻止点击弹框内部时关闭弹框
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 标题：水平居中，垂直占弹框80%的上方显示（即顶部20%的位置，约30dp）
                    Text(
                        text = "有说有笑",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF212121),
                        modifier = Modifier
                            .padding(top = 30.dp) // 垂直占弹框80%的上方，即顶部20%的位置（150 * 0.2 = 30dp）
                    )
                    
                    // 内容区域：160dp * 80dp，水平居中，在标题下方20dp
                    Box(
                        modifier = Modifier
                            .size(width = 160.dp, height = 80.dp)
                            .padding(top = 20.dp)
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "今天昨天、明天、未来等待",
                            fontSize = 14.sp,
                            color = Color(0xFF424242),
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * 详情页组件
 * 左侧区域占30%显示封面、标题、作者信息
 * 右侧区域占70%显示垂直滑动列表（标题+篇章数）
 * @param cardData 卡片数据
 * @param onBack 返回回调
 */
@Composable
fun DetailScreen(
    cardData: CardData,
    onBack: () -> Unit
) {
    // 生成章节列表数据（示例数据，实际应该从数据源获取）
    val chapters = remember {
        (1..20).map { index ->
            ChapterData(
                title = "第${index}章",
                chapterCount = Random.nextInt(10, 50) // 随机生成10-50的篇章数
            )
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2C2C2C)) // 灰黑色背景
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            // 左侧区域：占30%宽度，显示封面、标题、作者信息
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .fillMaxHeight()
                    .background(Color(0xFF1E1E1E)) // 稍深的背景色
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    // 封面
                    Image(
                        painter = painterResource(id = cardData.coverImage),
                        contentDescription = "封面",
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.5f) // 封面高度占左侧区域的50%
                            .padding(vertical = 16.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // 标题
                    Text(
                        text = cardData.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // 作者
                    Text(
                        text = cardData.author,
                        fontSize = 14.sp,
                        color = Color(0xFFCCCCCC),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                // 返回按钮：位于左上角，使用Box层叠在最上层
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .size(40.dp)
                        .background(
                            color = Color(0xFF3C3C3C).copy(alpha = 0.8f),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable { onBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "返回",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            
            // 右侧区域：占70%宽度，显示垂直滑动列表
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .fillMaxHeight()
                    .background(Color(0xFF2C2C2C))
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(chapters) { chapter ->
                        ChapterListItem(
                            chapter = chapter,
                            onClick = {
                                // 可以在这里处理章节点击事件
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * 章节列表项组件
 * 显示标题和篇章数
 * @param chapter 章节数据
 * @param onClick 点击回调
 */
@Composable
fun ChapterListItem(
    chapter: ChapterData,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFF3C3C3C),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 标题
            Text(
                text = chapter.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
            
            // 篇章数
            Text(
                text = "${chapter.chapterCount}篇",
                fontSize = 14.sp,
                color = Color(0xFFCCCCCC),
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

/**
 * 全局悬浮球组件
 * 默认显示在左侧垂直居中位置，支持拖拽移动
 * 优先级最高，显示在最上层，不影响用户操作其他事件
 */
@Composable
fun FloatingBall() {
    val density = LocalDensity.current
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    // 悬浮球尺寸
    val ballSize = 56.dp
    
    // 使用BoxWithConstraints获取屏幕尺寸
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        // 获取屏幕尺寸
        val screenWidth = with(density) { maxWidth.toPx() }
        val screenHeight = with(density) { maxHeight.toPx() }
        val ballSizePx = with(density) { ballSize.toPx() }
        
        // 默认位置：左侧垂直居中
        val defaultX = 0f
        val defaultY = (screenHeight - ballSizePx) / 2f
        
        // 保存悬浮球位置状态
        var offsetX by remember { mutableStateOf(defaultX) }
        var offsetY by remember { mutableStateOf(defaultY) }
        
        // 拖拽时的临时偏移量
        var dragOffsetX by remember { mutableStateOf(0f) }
        var dragOffsetY by remember { mutableStateOf(0f) }
        // 跟踪是否正在拖拽，用于阻止点击事件
        var isDragging by remember { mutableStateOf(false) }
        
        Box(
            modifier = Modifier
                .offset {
                    // 计算最终位置，限制在屏幕范围内
                    val finalX = (offsetX + dragOffsetX).coerceIn(
                        0f,
                        screenWidth - ballSizePx
                    )
                    val finalY = (offsetY + dragOffsetY).coerceIn(
                        0f,
                        screenHeight - ballSizePx
                    )
                    IntOffset(finalX.roundToInt(), finalY.roundToInt())
                }
                .size(ballSize)
                .pointerInput(Unit) {
                    // 使用 detectDragGestures 检测拖拽（优先级高）
                    detectDragGestures(
                        onDragStart = {
                            // 拖拽开始，标记为正在拖拽，阻止点击事件
                            Log.i("YYT", "拖拽开始")
                            isDragging = true
                            dragOffsetX = 0f
                            dragOffsetY = 0f
                        },
                        onDrag = { change, dragAmount ->
                            // 更新临时偏移量
                            dragOffsetX += dragAmount.x
                            dragOffsetY += dragAmount.y
                        },
                        onDragEnd = {
                            // 拖拽结束，更新最终位置
                            Log.i("YYT", "拖拽结束，更新位置")
                            offsetX = (offsetX + dragOffsetX).coerceIn(
                                0f,
                                screenWidth - ballSizePx
                            )
                            offsetY = (offsetY + dragOffsetY).coerceIn(
                                0f,
                                screenHeight - ballSizePx
                            )
                            // 重置临时偏移量
                            dragOffsetX = 0f
                            dragOffsetY = 0f
                            // 延迟重置拖拽状态，避免立即触发点击
                            coroutineScope.launch {
                                delay(150)
                                isDragging = false
                            }
                        }
                    )
                }
                .clickable(enabled = !isDragging) {
                    // 使用 clickable 处理点击（只有在没有拖拽时才启用）
                    if (!isDragging) {
                        Toast.makeText(context, "功能开发中。。。", Toast.LENGTH_SHORT).show()
                        Log.i("YYT", "点击悬浮球")
                    }
                }
        ) {
            // 悬浮球UI - 优化样式
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        alpha = 0.85f // 整体透明度
                        shape = RoundedCornerShape(28.dp) // 在图形层设置圆形形状
                        clip = true // 启用裁剪
                    }
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(28.dp),
                        spotColor = Color.Black.copy(alpha = 0.3f)
                    )
                    .clip(RoundedCornerShape(28.dp)) // 裁剪为圆形，确保所有内容都是圆形
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFFEC0934).copy(alpha = 0.7f), // 中心较不透明
                                Color(0xFFEC0934).copy(alpha = 0.5f)  // 边缘较透明
                            ),
                            center = Offset(28f, 28f),
                            radius = 28f
                        ),
                        shape = RoundedCornerShape(28.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                // 图标或文字 - 使用渐变效果
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            color = Color.White.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "⚙",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}



