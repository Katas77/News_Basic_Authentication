package com.example.News_Basic_Authentication.web.dto.news;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class createNewsRequest {

    @NotBlank(message = "поле категория   должно быть заполнено!")
    @Size(min = 3, max = 30, message = "поле категория не может быть меньше {min} и больше {max}!")
    private String category;


    @NotBlank(message = "поле новостей   должно быть заполнено!")
    @Size(min = 3, max = 300, message = "поле новостей не может быть меньше {min} и больше {max}!")
    private String text;

}
