package uk.co.techswitch.myface.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import uk.co.techswitch.myface.models.api.ResultsPage;
import uk.co.techswitch.myface.models.api.ResultsPageBuilder;
import uk.co.techswitch.myface.models.api.comments.CommentsFilter;
import uk.co.techswitch.myface.models.api.comments.CommentModel;
import uk.co.techswitch.myface.models.api.users.UserModel;
import uk.co.techswitch.myface.models.database.Comment;
import uk.co.techswitch.myface.services.CommentsService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/comments")
public class CommentsController {
    private final CommentsService commentsService;

    public CommentsController(CommentsService commentsService) {
        this.commentsService = commentsService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView searchComments(CommentsFilter filter) {
        List<Comment> comments = commentsService.searchComments(filter);
        int numberMatchingSearch = commentsService.countComments(filter);

        ResultsPage results = new ResultsPageBuilder<CommentModel, CommentsFilter>()
                .withItems(comments.stream().map(CommentModel::new).collect(Collectors.toList()))
                .withFilter(filter)
                .withNumberMatchingSearch(numberMatchingSearch)
                .withBaseUrl("/comments")
                .build();

        return new ModelAndView("comments/search", "results", results);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ModelAndView getCommentDetails(@PathVariable("id") long id) {
        Comment comment = commentsService.getById(id);

        CommentModel model = new CommentModel(comment);

        return new ModelAndView("/comments/detail", "commentModel", model);
    }
}