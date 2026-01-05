import numpy as np

seed = 22102017
rng = np.random.RandomState(seed)

class L1Penalty(object):
    def __init__(self, coefficient):
        assert coefficient > 0., 'Penalty coefficient must be positive.'
        self.coefficient = coefficient
    def __call__(self, parameter):
        return self.coefficient * np.sum(np.abs(parameter))
    def grad(self, parameter):
        return self.coefficient * np.sign(parameter)
    def __repr__(self):
        return 'L1Penalty({0})'.format(self.coefficient)

class L2Penalty(object):
    def __init__(self, coefficient):
        assert coefficient > 0., 'Penalty coefficient must be positive.'
        self.coefficient = coefficient
    def __call__(self, parameter):
        return self.coefficient * np.sum(parameter ** 2)
    def grad(self, parameter):
        return 2 * self.coefficient * parameter
    def __repr__(self):
        return 'L2Penalty({0})'.format(self.coefficient)

class L1L2MixPenalty(object):
    def __init__(self, coefficient_l1, coefficient_l2):
        assert coefficient_l1 >= 0., 'L1 coefficient must be non-negative.'
        assert coefficient_l2 >= 0., 'L2 coefficient must be non-negative.'
        self.coefficient_l1 = coefficient_l1
        self.coefficient_l2 = coefficient_l2
    def __call__(self, parameter):
        return self.coefficient_l1 * np.sum(np.abs(parameter)) + self.coefficient_l2 * np.sum(parameter ** 2)
    def grad(self, parameter):
        return self.coefficient_l1 * np.sign(parameter) + 2 * self.coefficient_l2 * parameter
    def __repr__(self):
        return 'L1L2MixPenalty(L1={0}, L2={1})'.format(self.coefficient_l1, self.coefficient_l2)
