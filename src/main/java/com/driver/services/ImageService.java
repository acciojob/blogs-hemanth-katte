package com.driver.services;

import com.driver.models.*;
import com.driver.repositories.BlogRepository;
import com.driver.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageService {
    @Autowired
    ImageRepository imageRepository2;

    @Autowired
    BlogRepository blogRepository;

    public Image createAndReturn(Blog blog, String description, String dimensions){
        //create an image based on given parameters and add it to the imageList of given blog
        Image newImage = new Image();
        newImage.setDescription(description);
        newImage.setDimensions(dimensions);
       // newImage.setBlog(blog);

        Blog blogFromRepo = blogRepository.findById(blog.getId()).get();

        newImage.setBlog(blogFromRepo);

        List<Image> imageList = blogFromRepo.getImageList();

        imageList.add(newImage);

        blogFromRepo.setImageList(imageList);

        blogRepository.save(blogFromRepo);

        return newImage;
    }

    public void deleteImage(Image image){

        Image imageToBeDeleted = imageRepository2.findById(image.getId()).get();

        int blogId = imageToBeDeleted.getBlog().getId();

        Blog blog = blogRepository.findById(blogId).get();

        List<Image> blogImageList = blog.getImageList();

        blogImageList.remove(imageToBeDeleted);

        blog.setImageList(blogImageList);

        blogRepository.save(blog);

        imageRepository2.deleteById(image.getId());
    }

    public Image findById(int id) {

        return imageRepository2.findById(id).get();
    }

    public int countImagesInScreen(Image image, String screenDimensions) {
        //Find the number of images of given dimensions that can fit in a screen having `screenDimensions`
        //In case the image is null, return 0
        //Image image = imageRepository2.findById(id).get();

        if(image == null)
            return 0;

        int imageDimensions = extractInteger(image.getDimensions());

        int screenDimensions1 = extractInteger(screenDimensions);

        return screenDimensions1/imageDimensions;

    }

    public int extractInteger(String dimensions){

        String[] dimensionsArray = dimensions.split("X");

        return Integer.valueOf(dimensionsArray[0]) * Integer.valueOf(dimensionsArray[1]);
    }



}
