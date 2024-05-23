package com.example.News_Basic_Authentication.web.dto.comment;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class createCommentRequest {


    @NotNull(message = "ID новости  должно быть указано")
    @Positive(message = "ID новости должно быть больше 0!")
    private Long news_id;


    @NotBlank(message = "поле коментарий    должно быть заполнено!")
    @Size(min = 3, max = 80, message = "поле коментарий  не может быть меньше {min} и больше {max}!")
    private String text;

}
