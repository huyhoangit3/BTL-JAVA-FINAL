class Point:
    # ham tao
    def __init__(self):
        self.x = 0
        self.y = 0

    # override equal method
    def __eq__(self, other):
        return isinstance(other, Point) and self.x == other.x \
               and self.y == other.y
    # make Point object hashable
    def __hash__(self):
    	return hash((self.x, self.y))