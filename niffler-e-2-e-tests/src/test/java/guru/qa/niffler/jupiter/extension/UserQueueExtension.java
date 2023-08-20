package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class UserQueueExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {
    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserQueueExtension.class);
    private static Map<User.UserType, Queue<UserJson>> usersQueue = new ConcurrentHashMap<>();

    static {
        Queue<UserJson> usersWithFriends = new ConcurrentLinkedQueue<>();
        usersWithFriends.add(bindUser("German", "12345"));
        usersWithFriends.add(bindUser("Barsik", "12345"));
        usersQueue.put(User.UserType.WITH_FRIENDS, usersWithFriends);

        Queue<UserJson> usersInSent = new ConcurrentLinkedQueue<>();
        usersInSent.add(bindUser("Bee", "12345"));
        usersInSent.add(bindUser("Anna", "12345"));
        usersQueue.put(User.UserType.INVITATION_SENT, usersInSent);

        Queue<UserJson> usersInRc = new ConcurrentLinkedQueue<>();
        usersInRc.add(bindUser("Valentin", "12345"));
        usersInRc.add(bindUser("Pizzly", "12345"));
        usersQueue.put(User.UserType.INVITATION_RECEIVED, usersInRc);
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Method[] methods = context.getRequiredTestClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(BeforeEach.class)) {
                Parameter[] parameters = method.getParameters();
                for (Parameter parameter : parameters) {
                    if (parameter.getType().isAssignableFrom(UserJson.class) && parameter.isAnnotationPresent(User.class)) {
                        User parameterAnnotation = parameter.getAnnotation(User.class);
                        User.UserType userType = parameterAnnotation.userType();
                        Queue<UserJson> usersQueueByType = usersQueue.get(parameterAnnotation.userType());
                        UserJson candidateForTest = null;
                        while (candidateForTest == null) {
                            candidateForTest = usersQueueByType.poll();
                        }
                        candidateForTest.setUserType(userType);
                        context.getStore(NAMESPACE).put(getAllureId(context), candidateForTest);
                        break;
                    }
                }
                break;
            }
        }
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        UserJson userFromTest = context.getStore(NAMESPACE).get(getAllureId(context), UserJson.class);
        usersQueue.get(userFromTest.getUserType()).add(userFromTest);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class)
                && parameterContext.getParameter().isAnnotationPresent(User.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(getAllureId(extensionContext), UserJson.class);
    }

    private String getAllureId(ExtensionContext context) {
        AllureId allureId = context.getRequiredTestMethod().getAnnotation(AllureId.class);
        if (allureId == null) {
            throw new IllegalStateException("Annotation @AllureId must be present!");
        }
        return allureId.value();
    }

    private static UserJson bindUser(String username, String password) {
        UserJson user = new UserJson();
        user.setUsername(username);
        user.setPassword(password);
        return user;
    }
}
