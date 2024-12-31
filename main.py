import numpy as np
import matplotlib.pyplot as plt
from tensorflow.keras.datasets import mnist
import tensorflow.keras as keras
from sklearn.preprocessing import OneHotEncoder
from sklearn.model_selection import train_test_split


def load_and_preprocess_data():
    """Загрузка и предобработка данных MNIST."""
    # Загрузка данных MNIST
    (train_images, train_labels), (test_images, test_labels) = mnist.load_data()

    # Нормализация данных
    train_images = train_images.reshape(-1, 28 * 28) / 255.0
    test_images = test_images.reshape(-1, 28 * 28) / 255.0

    # Преобразование меток в формат OneHotEncoding
    train_labels = train_labels.reshape(-1, 1)
    test_labels = test_labels.reshape(-1, 1)
    label_encoder = OneHotEncoder(categories="auto")
    encoded_train_labels = label_encoder.fit_transform(train_labels).toarray()
    encoded_test_labels = label_encoder.transform(test_labels).toarray()

    # Разделение тренировочных данных для обучения и валидации
    train_images, val_images, train_labels, val_labels = train_test_split(
        train_images, encoded_train_labels, test_size=0.2, random_state=42
    )

    return train_images, val_images, train_labels, val_labels, test_images, encoded_test_labels


def display_image(image_data):
    """Отображение изображения."""
    plt.imshow(image_data.reshape((28, 28)), cmap="gray")
    plt.show()


def build_neural_network():
    """Создание нейронной сети."""
    model = keras.Sequential([
        keras.Input(shape=(28 * 28,)),
        keras.layers.Dense(units=128, activation='relu'),
        keras.layers.Dense(10, activation='softmax')
    ])
    model.compile(optimizer='sgd', loss='categorical_crossentropy', metrics=['accuracy'])
    return model


def train_model(model, train_images, train_labels, val_images, val_labels, epochs=20, batch_size=128):
    """Обучение модели."""
    model.fit(
        train_images, train_labels,
        validation_data=(val_images, val_labels),
        epochs=epochs,
        batch_size=batch_size
    )


def evaluate_model(model, test_images, test_labels):
    """Оценка модели на тестовых данных."""
    test_loss, test_accuracy = model.evaluate(test_images, test_labels, verbose=0)
    print(f"Test Accuracy: {test_accuracy:.2f}, Test Loss: {test_loss:.2f}")
    return test_loss, test_accuracy


def predict_image(model, image):
    """Предсказание для изображения."""
    prediction = model.predict(image.reshape((1, -1)))
    predicted_label = np.argmax(prediction)
    return predicted_label


def main():
    """Основная функция."""
    # Загрузка и предобработка данных
    train_images, val_images, train_labels, val_labels, test_images, test_labels = load_and_preprocess_data()

    # Отображение примера изображения из тренировочных данных
    print("Displaying a sample image from the training set:")
    display_image(train_images[25])

    # Создание и обучение модели
    neural_net = build_neural_network()
    train_model(neural_net, train_images, train_labels, val_images, val_labels)

    # Оценка модели
    evaluate_model(neural_net, test_images, test_labels)

    # Демонстрация работы предсказания
    test_sample_index = 80
    print("Displaying a sample image from the test set:")
    display_image(test_images[test_sample_index])

    predicted_label = predict_image(neural_net, test_images[test_sample_index])
    print(f"Predicted number for the image: {predicted_label}")


if __name__ == "__main__":
    main()