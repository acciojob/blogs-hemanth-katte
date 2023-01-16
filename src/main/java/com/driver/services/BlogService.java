package com.driver.services;

import com.driver.models.Blog;
import com.driver.models.Image;
import com.driver.models.User;
import com.driver.repositories.BlogRepository;
import com.driver.repositories.ImageRepository;
import com.driver.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class BlogService {
    @Autowired
    BlogRepository blogRepository1;

    @Autowired
    ImageService imageService1; // = new ImageService();

    @Autowired
    UserRepository userRepository1;

    public List<Blog> showBlogs(){
        //find all blogs
        List<Blog> blogList = blogRepository1.findAll();

        return blogList;

    }

    public void createAndReturnBlog(Integer userId, String title, String content) {
        //create a blog at the current time
        Blog newBlog = new Blog();
        newBlog.setTitle(title);
        newBlog.setContent(content);
        newBlog.setPubDate(new Date());

        User user = userRepository1.findById(userId).get();

        newBlog.setUser(user);

        List<Blog> userBlogList = user.getBlogList();

        userBlogList.add(newBlog);

        user.setBlogList(userBlogList);

        blogRepository1.save(newBlog);
        userRepository1.save(user);

        //updating the blog details

        //Updating the userInformation and changing its blogs

    }

    public Blog findBlogById(int blogId){
        //find a blog
        return blogRepository1.findById(blogId).get();
    }

    public void addImage(Integer blogId, String description, String dimensions){
        //add an image to the blog after creating it
       // Image newImage = new Image();
       // newImage.setDescription(description);
       // newImage.setDimensions(dimensions);

        Blog blog = blogRepository1.findById(blogId).get();

        Image image=imageService1.createAndReturn(blog,description,dimensions);
        image.setBlog(blog);

        List<Image> imageList = blog.getImageList();

        if(imageList == null) imageList = new ArrayList<>();

        imageList.add(image);

        blog.setImageList(imageList);

        blogRepository1.save(blog);

    }

    public void deleteBlog(int blogId){
        //delete blog and corresponding images
        Blog blogToBeDeleted = blogRepository1.findById(blogId).get();

        if(blogToBeDeleted == null)
            return;

        int userId = blogToBeDeleted.getUser().getId();

        User user = userRepository1.findById(userId).get();

        List<Blog> userBlogs = user.getBlogList();

        userBlogs.remove(blogToBeDeleted);

        user.setBlogList(userBlogs);

        userRepository1.save(user);

        blogRepository1.deleteById(blogId);




    }
}
