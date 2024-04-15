package saur.org.vaadin.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import saur.org.vaadin.entity.Author;
import saur.org.vaadin.repository.AuthorRepository;

import java.util.List;

@Route("first")
public class MainView extends VerticalLayout {

    private final Grid<Author> grid = new Grid<>(Author.class, false);

    private final Button newBtn = new Button("New");
    private final Button deleteBtn = new Button("Delete");
    private final Button saveBtn = new Button("Save");

    private final HorizontalLayout btnLayout = new HorizontalLayout();
    private final HorizontalLayout fieldsLayout = new HorizontalLayout();

    private final TextField name = new TextField("First Name");
    private final TextField lastName = new TextField("Last Name");
    private Integer authorId = null;

    private final String MAX_WIDTH = "400px";
    private final String BUTTON_WIDTH = "123px";

    public MainView(AuthorRepository authorRepository) {
        grid.addColumn(Author::getAuthorId).setHeader("ID").setSortable(true).setWidth("20px");
        grid.addColumn(Author::getFirstName).setHeader("First name").setSortable(true);
        grid.addColumn(Author::getLastName).setHeader("Last name").setSortable(true);
        List<Author> data = authorRepository.findAll();
        grid.setItems(data);
        grid.addSelectionListener(selected -> {
            if (selected.getAllSelectedItems().isEmpty()) {
                deleteBtn.setEnabled(false);
                clearInputFields();
                authorId = null;
            } else if (selected.getFirstSelectedItem().isPresent()) {
                deleteBtn.setEnabled(true);
                Author selectedCustomer = selected.getFirstSelectedItem().get();
                name.setValue(selectedCustomer.getFirstName());
                lastName.setValue(selectedCustomer.getLastName());
                authorId = selectedCustomer.getAuthorId();
            }
        });

        newBtn.setWidth(BUTTON_WIDTH);
        newBtn.addClickListener(click -> {
            clearInputFields();
            grid.select(null);
        });

        deleteBtn.setWidth(BUTTON_WIDTH);
        deleteBtn.setEnabled(false);
        deleteBtn.addClickListener(click -> {
            authorRepository.delete(grid.getSelectedItems().stream().toList().get(0));
            clearInputFields();
            grid.setItems(authorRepository.findAll());
        });

        saveBtn.setWidth(BUTTON_WIDTH);
        saveBtn.addClickListener(click -> {

            Author author = authorId == null ? new Author() : authorRepository.findById(authorId.longValue()).orElse(new Author());
            author.setFirstName(name.getValue());
            author.setLastName(lastName.getValue());

            authorRepository.save(author);

            clearInputFields();
            grid.setItems(authorRepository.findAll());
        });

        btnLayout.add(newBtn, deleteBtn, saveBtn);
        btnLayout.setMaxWidth(MAX_WIDTH);

        fieldsLayout.add(name, lastName);

        add(new NativeLabel("Hello world!"));
        add(grid);
        add(btnLayout);
        add(fieldsLayout);
    }

    private void clearInputFields() {
        name.clear();
        lastName.clear();
    }

}
