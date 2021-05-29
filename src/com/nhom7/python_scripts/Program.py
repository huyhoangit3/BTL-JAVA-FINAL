from Sensor import Sensor
import random
import sys
import matplotlib.pyplot as plt
import numpy as np
from mpl_toolkits.axes_grid1 import Divider, Size

class Program:
    # hàm tạo
    def __init__(self, length, width):
        self.length = length
        self.width = width
        # danh sách sensor
        self.sensor_list = list()

    # hàm đọc file
    def read_file(self, file_path):
        file = open(file_path, 'r')
        line_number = 0
        while True:
            line = file.readline()
            if line_number == 0:
                line_number += 1
                continue
            if not line:
                break
            line = line.rstrip('\n')
            result = line.split(',')
            result = [int(x) for x in result]
            sensor = Sensor()
            sensor.coordinate.x = result[0]
            sensor.coordinate.y = result[1]
            sensor.index = result[2]
            sensor.radius = result[3]
            self.sensor_list.append(sensor)
        file.close()

    def find_neighbors(self, source):
        for sensor in self.sensor_list:
            if source != sensor and source.distance_to_other(sensor) <= 2 * source.radius:
                source.near_neighbors.append(sensor.index)

    def find_all_neighbors(self):
        for sensor in self.sensor_list:
            self.find_neighbors(sensor)

    def find_all_shortest_path(self):
        for i in range(1, len(self.sensor_list)):
            self.sensor_list[i].shortest_path = self.find_shortest_path(0, i)

    def find_shortest_path(self, sour, dest):
        pred = [None] * len(self.sensor_list)
        dist = [None] * len(self.sensor_list)
        if self.BFS(sour, dest, pred, dist) == False:
            return None
        path = list()
        crawl = dest
        path.append(crawl)
        while pred[crawl] != -1:
            path.append(pred[crawl])
            crawl = pred[crawl]
        path = path[::-1]
        return path

    def init_data(self):
        self.find_all_neighbors()
        self.find_all_shortest_path()

    def BFS(self, sour, dest, pred, dist):
        queue = list()
        visited = [None] * len(self.sensor_list)
        for i in range(len(self.sensor_list)):
            visited[i] = False
            dist[i] = sys.float_info.max
            pred[i] = -1
        visited[sour] = True
        dist[sour] = 0
        queue.append(sour)

        while len(queue) != 0:
            u = queue.pop(0)
            neighbors = self.sensor_list[u].near_neighbors
            for neighbor in neighbors:
                if visited[neighbor] == False:
                    visited[neighbor] = True
                    dist[neighbor] = dist[u] + 1
                    pred[neighbor] = u
                    queue.append(neighbor)
                    if neighbor == dest:
                        return True
        return False

    def get_data_figure(self, xlim, ylim, mode):
        # danh sách các tọa độ x
        x_values = list()
        # danh sách các tọa độ y
        y_values = list()
        # danh sách các lable là số thứ tự của từng sensor
        labels = list()

        # vẽ danh sách các sensor
        drawing_circle_list = list()
        drawing_sink_circle = None

        if mode == 0:
            for i in range(len(self.sensor_list)):
                sensor = self.sensor_list[i]
                if i == 0:
                    drawing_sink_circle = plt.Circle((sensor.coordinate.x, sensor.coordinate.y), sensor.radius, color='#fb8500')
                else:
                    if sensor.shortest_path != None:
                        drawing_circle = plt.Circle((sensor.coordinate.x, sensor.coordinate.y), sensor.radius, color='#54e346')
                    else:
                        drawing_circle = plt.Circle((sensor.coordinate.x, sensor.coordinate.y), sensor.radius, color='#b7b7a4')
                    drawing_circle_list.append(drawing_circle)
                    x_values.append(sensor.coordinate.x)
                    y_values.append(sensor.coordinate.y)
                    labels.append(sensor.index)
        else:
            active_sensors = self.sensor_list[mode].shortest_path
            drawing_group_circle = list()
            for i in range(len(self.sensor_list)):
                sensor = self.sensor_list[i]
                if i == 0:
                    drawing_sink_circle = plt.Circle((sensor.coordinate.x, sensor.coordinate.y), sensor.radius, color='#fb8500')
                else:
                    if sensor.index in active_sensors:
                        if sensor == self.sensor_list[mode]:
                            drawing_group_circle.append(plt.Circle((sensor.coordinate.x, sensor.coordinate.y), sensor.radius, color='#ffd60a'))
                        else:
                            drawing_group_circle.insert(0, plt.Circle((sensor.coordinate.x, sensor.coordinate.y), sensor.radius, color='#54e346'))
                    else:
                        drawing_circle = plt.Circle((sensor.coordinate.x, sensor.coordinate.y), sensor.radius, color='#b7b7a4')
                        drawing_circle_list.append(drawing_circle)
            for s in active_sensors:
                x_values.append(self.sensor_list[s].coordinate.x)
                y_values.append(self.sensor_list[s].coordinate.y)
                labels.append(self.sensor_list[s].index)
            drawing_circle_list.extend(drawing_group_circle)

        drawing_circle_list.append(drawing_sink_circle)
        x_values.append(self.sensor_list[0].coordinate.x)
        y_values.append(self.sensor_list[0].coordinate.y)
        labels.append(self.sensor_list[0].index)

        # danh sách các điểm để vẽ
        x_points = np.array(x_values)
        y_points = np.array(y_values)

        return [x_points, y_points, labels, drawing_circle_list]

    def draw(self, xlim, ylim, mode):
        x_points, y_points, labels, drawing_circle_list = self.get_data_figure(xlim, ylim, mode)
        figure = plt.figure(figsize=(8, 6.3))
        # The first items are for padding and the second items are for the axes.
        # sizes are in inch.
        h = [Size.Fixed(0.2), Size.Fixed(7.8)]
        v = [Size.Fixed(0.3), Size.Fixed(5.9)]

        divider = Divider(figure, (0, 0, 1, 1), h, v, aspect=False)
        # The width and height of the rectangle are ignored.

        axes = figure.add_axes(divider.get_position(),
                          axes_locator=divider.new_locator(nx=1, ny=1))
        axes.scatter(x_points, y_points)
        axes.set_aspect(1)

        for i in drawing_circle_list:
            # vẽ danh sách các sensor đã random
            axes.add_artist(i)

        # vẽ các số thứ tự tương ứng
        for i, txt in enumerate(labels):
            axes.annotate(txt, (x_points[i], y_points[i]))

        # vẽ các điểm ứng với các tọa độ các sensor đã random không có đường nối
        plt.plot(x_points, y_points, ".")
        # set giá trị limit cho trục x và y
        plt.xlim([0, xlim])
        plt.ylim([0, ylim])
        plt.show()