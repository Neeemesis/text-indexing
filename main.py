import nltk
import numpy as np
from nltk.stem.lancaster import LancasterStemmer
import tensorflow
import tflearn
import random
import json

# Инициализация стеммера
stemmer = LancasterStemmer()


def load_intents(file_path):
    """Загрузка данных из JSON файла."""
    with open(file_path) as file:
        return json.load(file)


def preprocess_data(intents):
    """Предобработка данных: токенизация, создание мешка слов и меток."""
    all_words = []
    tags = []
    patterns = []
    tag_labels = []

    for intent in intents['intents']:
        for phrase in intent['patterns']:
            tokenized_words = nltk.word_tokenize(phrase)  # Токенизация фраз
            all_words.extend(tokenized_words)
            patterns.append(tokenized_words)
            tag_labels.append(intent["tag"])

        if intent['tag'] not in tags:
            tags.append(intent["tag"])

    # Обработка слов
    all_words = [stemmer.stem(word.lower()) for word in all_words if word != "?"]
    all_words = sorted(list(set(all_words)))

    tags = sorted(tags)

    return all_words, tags, patterns, tag_labels


def create_training_data(all_words, tags, patterns, tag_labels):
    """Создание обучающих данных из предобработанных входных данных."""
    training_data = []
    output_data = []

    empty_output = [0 for _ in range(len(tags))]

    for index, pattern in enumerate(patterns):
        bag_of_words = []

        stemmed_words = [stemmer.stem(word.lower()) for word in pattern]

        for word in all_words:
            if word in stemmed_words:
                bag_of_words.append(1)
            else:
                bag_of_words.append(0)

        output_row = empty_output[:]
        output_row[tags.index(tag_labels[index])] = 1

        training_data.append(bag_of_words)
        output_data.append(output_row)

    return np.array(training_data), np.array(output_data)


def build_model(input_size, output_size):
    """Построение нейронной сети с заданными входами и выходами."""
    tensorflow.compat.v1.reset_default_graph()

    neural_net = tflearn.input_data(shape=[None, input_size])
    neural_net = tflearn.fully_connected(neural_net, 8)
    neural_net = tflearn.fully_connected(neural_net, 8)
    neural_net = tflearn.fully_connected(neural_net, output_size, activation="softmax")
    neural_net = tflearn.regression(neural_net)

    return tflearn.DNN(neural_net)


def train_model(model, training_data, output_data, n_epoch=1500, batch_size=8):
    """Обучение модели и сохранение весов."""
    model.fit(training_data, output_data, n_epoch=n_epoch, batch_size=batch_size, show_metric=True)
    model.save("chatbot_model.tflearn")


def generate_bag_of_words(user_input, word_list):
    """Генерация мешка слов для пользовательского ввода."""
    bag = [0 for _ in range(len(word_list))]
    tokenized_input = nltk.word_tokenize(user_input)
    stemmed_input = [stemmer.stem(word.lower()) for word in tokenized_input]
    for token in stemmed_input:
        for index, word in enumerate(word_list):
            if word == token:
                bag[index] = 1
    return np.array(bag)


def chatbot(model, intents, all_words, tags):
    """Функция чата с пользователем."""
    print("Chatbot is online! Type 'quit' to exit.")
    while True:
        user_input = input("\nYou: ")
        if user_input.lower() == 'quit':
            print("Goodbye!")
            break

        results = model.predict([generate_bag_of_words(user_input, all_words)])
        results_index = np.argmax(results)
        tag = tags[results_index]

        for intent in intents['intents']:
            if intent['tag'] == tag:
                response = random.choice(intent['responses'])
        print(f"Bot: {response}")


def main():
    """Основная функция программы."""
    intents = load_intents('intents.json')
    all_words, tags, patterns, tag_labels = preprocess_data(intents)
    training_data, output_data = create_training_data(all_words, tags, patterns, tag_labels)

    # Построение и обучение модели
    model = build_model(len(training_data[0]), len(output_data[0]))
    train_model(model, training_data, output_data)

    # Запуск чата
    chatbot(model, intents, all_words, tags)


if __name__ == "__main__":
    main()