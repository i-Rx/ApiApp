package com.example.apiapp.presentation.screens.popular

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.apiapp.R
import com.example.apiapp.contest.MOVIE_IMAGE_BASE_URL
import com.example.apiapp.model.BackdropSize
import com.example.apiapp.model.Results
import com.example.apiapp.presentation.navigation.Screens
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun PopularMoviesScreen(
    navController: NavHostController,
    popularMoviesState: MutableStateFlow<PagingData<Results>>
) {
    val moviePagingItems = popularMoviesState.collectAsLazyPagingItems()
    Box {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalArrangement = Arrangement.Center
        ) {
            items(moviePagingItems.itemCount) { index ->
                if(moviePagingItems[index]?.adult==false) {
                    AsyncImage(

                        model = "${MOVIE_IMAGE_BASE_URL}${BackdropSize.w300}/${moviePagingItems[index]?.posterPath}",
                        contentDescription = "",
                        modifier = Modifier
                            .padding(2.dp)
                            .clickable{
                                navController.navigate(Screens.MovieDetail.route + "/${moviePagingItems[index]?.id}")
                            },
                        contentScale = ContentScale.FillWidth,
                        error = painterResource(R.drawable.no_poster),
                        placeholder = painterResource(R.drawable.no_poster)
                    )
                }
            }
        }
        moviePagingItems.apply {
            when {
                loadState.refresh is LoadState.Loading -> {

                    Row(
                        Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator()
                    }
                }

                loadState.refresh is LoadState.Error -> {
                    val error = moviePagingItems.loadState.refresh as LoadState.Error
                    Row(
                        Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = error.error.localizedMessage.orEmpty(),
                            modifier = Modifier,
                        )
                    }
                }

                loadState.append is LoadState.Loading -> {
                    Row(
                        Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        CircularProgressIndicator()
                    }
                }

                loadState.append is LoadState.Error -> {
                    val error = moviePagingItems.loadState.append as LoadState.Error
                    Text(
                        text = error.error.localizedMessage.orEmpty(),
                        modifier = Modifier,
                    )
                }
            }
        }
    }

}